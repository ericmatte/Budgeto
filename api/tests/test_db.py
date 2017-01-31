import pytest
from flask import g

from lib.time_calculator import TimeCalculator
from models import Keyword
from models import Transaction
from models.user import User
from tests.conftest import set_current_user


def test_db(client):
    users = User.get_all()
    assert len(users) > 0


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
            assertion(Transaction.get_all_hierarchically(g.user))
            t.get_running_time()
