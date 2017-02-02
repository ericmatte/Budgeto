from datetime import date
from flask import g
from flask import json

from models import Transaction
from models.limit import Limit
from tests import set_current_user


def fetch_transaction(client):
    # TODO: Make it work.
    with set_current_user():
        data = {}
        with open("tests/resources/test_transactions.txt", 'r') as file:
            data['transactions'] = file.read()

        response = client.post('/budgeto/fetch-transactions', data=data)
        assert response.status_code == 200


def test_add_transaction(client):
    with set_current_user():
        data = {'desc': 'Test de transaction', 'amount': '10.55', 'date': date.today().strftime("%d-%m-%Y"),
                'bank': '2', 'cat': '2'}
        response = client.post('/budgeto/add-transaction', data=data)
        assert response.status_code == 201

        transaction = Transaction.get(uuid=json.loads(response.data)['transaction'])
        assert transaction.user == g.user
        assert transaction.bank_id == int(data['bank'])
        assert transaction.description == data['desc']
        assert transaction.category_id == int(data['cat'])
        assert transaction.amount == float(data['amount'])
        assert transaction.date.strftime("%d-%m-%Y") == data['date']


def test_set_limit(client):
    with set_current_user():
        post_limit = lambda data : client.post('/budgeto/save-category-options', data=data)

        # Test add limit
        data = {'cat': '36', 'setLimit':'on', 'limit': '12334'}
        response = post_limit(data)
        assert response.status_code == 200

        limit = Limit.get(user=g.user, category_id=data['cat'])
        assert limit.value == float(data['limit'])

        # Test wrong data
        response = post_limit({'cat': '36', 'limit': '-145'})
        assert response.status_code == 200

        response = post_limit({'cat': '36', 'setLimit':'on'})
        assert response.status_code == 200

        response = post_limit({'cat': '36', 'setLimit':'on', 'limit': '-145'})
        assert response.status_code == 200

        response = post_limit({'cat': '36', 'setLimit':'on', 'limit': ''})
        assert response.status_code == 200
        assert Limit.get(user=g.user, category_id=data['cat']) is None

        # Test removing limit
        response = post_limit({'cat': '36', 'setLimit':'off', 'limit': ''})
        assert response.status_code == 200
        assert Limit.get(user=g.user, category_id=data['cat']) is None



