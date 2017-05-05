import codecs
import json
from datetime import datetime

from flask import g
from flask import url_for

from endless import app
from endless import db_session
from endless.budgeto.backend import put_transactions
from endless.main.services import update_user
from models import Transaction
from models import User
from tests import set_current_user


def test_get_routes(client):
    with set_current_user():
        for route in app.url_map.iter_rules():
            if 'GET' in route.methods and route.endpoint != 'static':
                req = client.get(route.rule)
                print("Route to {0} validated with status '{1}'".format(route.rule, req.status))
                assert req.status_code < 400


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

def test_put_transaction(client, statements):
    with set_current_user():
        for statement in statements:
            with codecs.open(statement['json'], 'r', encoding='utf8') as f:
                ref = json.loads(f.read())
                now = datetime.now()

                assert put_transactions(statement, g.user)

                transactions = Transaction.get_all(Transaction.upload_time > now)
                assert len(transactions) == len(ref['transactions'])
                for i in range(0, len(transactions)):
                    assert transactions[i].description == ref['transactions']['description']
                    assert transactions[i].amount == ref['transactions']['amount']
                    assert transactions[i].date == ref['transactions']['date']
