from datetime import datetime
from flask import g
from models import Bank, add_to_db, set_attributes
from models import Category
from models import Transaction


def put_transactions(json_data, user):
    """Save a list of transactions into the database
    Args:
        json_data : str The json containing the bank and the transactions to add
        user : User The user to add to
    Returns:
        The number of transactions added
    """
    bank = Bank.get(name=json_data.get('bank'))
    if bank is None:
        raise ValueError('Bank not supported.')

    added_transactions = 0
    for t in json_data.get('transactions', []):
        transaction = set_attributes(Transaction(), user_id=user.user_id, bank_id=bank.bank_id)
        add_to_db(transaction, **t)
        added_transactions += 1

    return added_transactions

def get_transaction_attributes(transaction):
    t_attributes = {
        'user_id': g.user.user_id,
        'bank_id': int(transaction['bank_id']),
        'category_id': int(transaction['category_id']),
        'description': transaction['description'] or Category.get(category_id=int(transaction['category_id'])).name,
        'amount': float(transaction['amount']),
        'date': datetime.strptime(transaction['date'], '%Y-%m-%d')
    }
    return t_attributes