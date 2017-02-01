from datetime import date
from flask import g
from flask import json

from models import Transaction
from tests import set_current_user


def test_fetch_transaction(client):
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
        assert transaction.category_id == data['cat']
        assert transaction.amount == float(data['amount'])
        assert transaction.date.strftime("%d-%m-%Y") == data['date']


