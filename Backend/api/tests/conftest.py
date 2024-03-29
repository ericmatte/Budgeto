import pytest
from datetime import datetime

from sqlalchemy import event

from endless.flask import app, db_session
from models import Transaction
from models import User

@pytest.fixture
def client(request):
    app.config.update({ 'TESTING': True })

    # start the session in a SAVEPOINT...
    db_session.begin_nested()
    # then each time that SAVEPOINT ends, reopen it
    @event.listens_for(db_session, "after_transaction_end")
    def restart_savepoint(session, transaction):
        if transaction.nested and not transaction._parent.nested:
            db_session.begin_nested()
    def teardown():
        db_session.rollback()
        db_session.close()

    request.addfinalizer(teardown)
    return app.test_client()

@pytest.fixture
def dummy_transaction(request):
    transaction = Transaction()
    transaction.user_id = 0
    transaction.bank_id = 0
    transaction.category_id = 0
    transaction.description = 'test'
    transaction.amount = 1337.37
    transaction.date = datetime.now()

    def teardown():
        pass # db_session.delete(transaction)  # transaction not even added to db at first

    request.addfinalizer(teardown)
    return transaction


@pytest.fixture
def dummy_google_user_info(request):
    user_info = {
        'at_hash': 'KW9YqqKkO214I35VTdGQZw',
        'aud': '567386675874-5fqmolj2dt7cu9g2dam7cheq7k923p8q.apps.googleusercontent.com',
        'azp': '567386675874-5fqmolj2dt7cu9g2dam7cheq7k923p8q.apps.googleusercontent.com',
        'iss': 'accounts.google.com',
        'iat': 1485265757,
        'exp': 1485269357,
        'sub': '107161240925642157925',
        'picture': 'https://lh4.googleusercontent.com/-zm0SY3XlTgY/AAAAAAAAAAI/AAAAAAAACyM/QqXcAAfeajU/s96-c/photo.jpg',
        'email': 'testemail@emailtest.com',
        'email_verified': True,
        'locale': 'en-CA',
        'given_name': 'Test',
        'family_name': 'Dummy',
        'name': 'Test Dummy'
    }
    user = User.get(google_id=user_info['sub'])
    if user is not None:
        db_session.delete(user)
        db_session.commit()

    def teardown():
        user = User.get(google_id=user_info['sub'])
        db_session.delete(user)
        db_session.commit()

    request.addfinalizer(teardown)
    return user_info

@pytest.fixture
def statements(request):
    path = 'tests/resources/csv/'
    statements = [
        {'bank': 'Desjardins', 'csv': 'Desjardins_Comptes.csv', 'json': 'Desjardins_Comptes.json'},
        {'bank': 'Tangerine', 'csv': 'Tangerine_Credit-Card.CSV', 'json': 'Tangerine_Credit-Card.json'},
        {'bank': 'Tangerine', 'csv': 'Tangerine_Saving-Account.CSV', 'json': 'Tangerine_Saving-Account.json'}
    ]
    for statement in statements:
        statement['csv'] = path + statement['csv']
        statement['json'] = path + statement['json']

    return statements


