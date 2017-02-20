from datetime import date, datetime
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

        transaction = Transaction.get_latest(Transaction.upload_time)
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


def test_transaction_fetcher(client):
    with set_current_user():
        post = lambda json_data : client.post('/budgeto/transactions-fetcher', data={'transactions': json.dumps(json_data)})
        truncate = lambda data, max: (data[:max - 3] + '...') if len(data) > max - 3 else data

        def validate_transaction(resp_t, json_t):
            print(json_t)
            assert resp_t.description == truncate(json_t['desc'], 256)
            assert resp_t.amount == float(json_t['amount'].replace(',','.') or 0)
            assert resp_t.date.strftime("%d-%m-%Y") == json_t['date'] or datetime.today()
            assert resp_t.bank.name == json_t['bank']

        with open("tests/resources/test_transactions_limit_cases.txt", 'r') as file:
            json_data = json.loads(file.read())

        response = post(json_data)
        assert response.status_code == 200
        fetched_transactions = json.loads(response.data)['count']
        transactions = Transaction.get_latest(Transaction.transaction_id, fetched_transactions)
        assert fetched_transactions == 12  # @see test_transactions_limit_cases.txt

        response_dict = {"":0, "bank":0, "date":0, "amount":0, "desc":0}
        for i in range(fetched_transactions):
            t = json_data[i]
            # Normal transaction validation
            validate_transaction(transactions[fetched_transactions-i-1], t)
            # Counting the number of success fetch
            response_dict[t['test']] += 1

        # Assertion of working transactions
        assert response_dict[""] == 2
        assert response_dict["desc"] == 1
        assert response_dict["bank"] == 4
        assert response_dict["date"] == 2
        assert response_dict["amount"] == 3

        # Testing duplicate values with one new value
        new_data = [json_data[0]]
        working_transaction = {"desc":"Working transaction","date":"23-01-2017","amount":"100.0","bank":"Tangerine"}
        new_data.append(working_transaction)
        response = post(new_data)
        assert response.status_code == 200
        fetched_transactions = json.loads(response.data)['count']
        assert fetched_transactions == 1  # @see test_transactions_limit_cases.txt
        transaction = Transaction.get_latest(Transaction.transaction_id)
        validate_transaction(transaction, working_transaction)



