from flask import g
from sqlalchemy import event

from endless import db_session
from lib.time_calculator import TimeCalculator
from models import Keyword, add_to_db
from models import Log
from models import Transaction
from models.user import User
from tests import set_current_user


def test_db(client):
    assert User.get(user_id=1) is not None


def test_get_all_keywords_hierarchically(client):
    def assertion(keywords):
        assert len(keywords) == 3
        assert len(keywords[1]['children']) > 0
        assert keywords[2]['count'] > 0
        assert type(keywords[3]['keywords']) is list

    t = TimeCalculator('keywords_hierarchically')
    assertion(Keyword.get_all_hierarchically())
    t.get_running_time()


def test_get_all_transactions_hierarchically(client):
    def assertion(transactions):
        assert len(transactions) == 3
        assert len(transactions[1]['children']) > 0
        assert transactions[2]['count'] > 0
        assert type(transactions[3]['transactions']) is list

    with set_current_user():
        t = TimeCalculator('transactions_hierarchically')
        assertion(Transaction.get_all_hierarchically(User.get(user_id=1)))
        t.get_running_time()
