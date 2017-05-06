import json
from datetime import date

from flask import g
from flask import jsonify

from endless import app
from endless import db_session
from endless.main.services import update_user
from models import Transaction, add_to_db
from models import User
from tests import set_current_user

def test_update_user(client, dummy_google_user_info):
    user = User.get(google_id=dummy_google_user_info['sub'])
    assert user is None

    # Test adding Google user to db
    update_user(dummy_google_user_info)
    user = User.get(google_id=dummy_google_user_info['sub'])
    assert user is not None
    assert user.email == dummy_google_user_info['email']
    assert user.full_name == dummy_google_user_info['name']

    # Test updating Google user in db
    new_email = 'dummyuser@dummytest.com'
    user.first_name = 'Second_test'
    user.last_name = 'Dummy_test'
    user.email = new_email
    db_session.commit()
    update_user(dummy_google_user_info)
    user = User.get(google_id=dummy_google_user_info['sub'])
    assert user.full_name == dummy_google_user_info['name']
    assert user.email == new_email

def test_fetch_initial_data(client):
    response = client.get('/budgeto/api/fetch-initial-data')
    assert response.status_code == 200
    data = json.loads(response.data.decode('utf-8'))
    assert len(data['banks']) > 0
    assert len(data['categories']) > 0
    assert data['categories']['1']['category_id'] == 1

def test_validate_token(client):
    with set_current_user():
        response = client.get('/budgeto/api/validate-token')
        assert response.status_code == 200
        user = json.loads(response.data.decode('utf-8'))
        assert user['user_id'] == g.user.user_id
        assert user['is_admin'] == g.user.is_admin

def test_get_transactions(client):
    def json_verifier(response, number_of_transactions):
        data = json.loads(response.data.decode('utf-8'))
        assert len(data.get('transactions', [])) == number_of_transactions

    with set_current_user():
        response = client.get('/budgeto/api/transactions')
        assert response.status_code == 200
        json_verifier(response, 0)

        add_to_db(Transaction(), description='Test de transaction', amount=10.55,
                  date=date.today(), bank_id=2, user_id=g.user.user_id)

        response = client.get('/budgeto/api/transactions')
        assert response.status_code == 200
        json_verifier(response, 1)

def test_get_routes(client):
    with set_current_user():
        for route in app.url_map.iter_rules():
            if 'GET' in route.methods and route.endpoint != 'static':
                req = client.get(route.rule)
                print("Route to {0} validated with status '{1}'".format(route.rule, req.status))
                assert req.status_code < 400