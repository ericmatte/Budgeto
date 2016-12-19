from contextlib import contextmanager
from datetime import datetime

from flask.globals import g
from flask.signals import appcontext_pushed
import pytest

from endless import app, db_session
from models import Transaction
from server.database import server


@pytest.fixture
def client(request):
    app.config['TESTING'] = True

    def teardown():
        server.stop()
    request.addfinalizer(teardown)
    return app.test_client()


@contextmanager
def set_current_user():
    def handler(sender, **kwargs):
        g.user = 'ericmatte.inbox@gmail.com'
    with appcontext_pushed.connected_to(handler, app):
        yield


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
        db_session.delete(transaction)

    request.addfinalizer(teardown)
    return transaction