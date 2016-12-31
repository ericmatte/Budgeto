def test_get_transaction(client, dummy_transaction):
    trans = dummy_transaction.__dict__
    print('test')
    assert True

def test_set_transaction(client, dummy_transaction):
    data = {'email': 'ericmatte.inbox@gmail.com'}
    with open("resources/test_transactions.txt", 'r') as file:
        data['transactions'] = file.read()

    response = client.post('/budgeto/fetch-transactions', data=data)
    assert response.status_code == 200