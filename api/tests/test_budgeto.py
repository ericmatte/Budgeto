from tests import set_current_user


def test_fetch_transaction(client):
    with set_current_user():
        data = {'email': 'ericmatte.inbox@gmail.com'}
        with open("tests/resources/test_transactions.txt", 'r') as file:
            data['transactions'] = file.read()

        response = client.post('/budgeto/fetch-transactions', data=data)
        assert response.status_code == 200