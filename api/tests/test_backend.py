import codecs
import json

from flask import g

from endless.budgeto.backend import put_transactions
from models import Transaction
from tests import set_current_user

def test_put_transaction(client, statements):
    with set_current_user():
        for statement in statements:
            with codecs.open(statement['json'], 'r', encoding='utf8') as f:
                json_data = json.loads(f.read())
                added_transactions =  put_transactions(json_data, g.user)
                assert added_transactions == len(json_data['transactions'])

                transactions = Transaction.get_latest(Transaction.transaction_id, added_transactions)
                for i in range(0, added_transactions):
                    inverse = added_transactions - i - 1
                    assert transactions[i].description == json_data['transactions'][inverse]['description']
                    assert transactions[i].amount == json_data['transactions'][inverse]['amount']
                    assert str(transactions[i].date) == json_data['transactions'][inverse]['date']
