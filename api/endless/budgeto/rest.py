from flask import g
from flask import json
from flask import make_response
from flask import request

from endless.budgeto import budgeto_rest
from endless.main.services import token_required
from lib.response_handler import HttpResponse
from models import Bank
from models import Category
from models import Transaction, to_json
from models.limit import Limit


@budgeto_rest.route('/fetch-initial-data', methods=['GET'])
def fetch_initial_data():
    banks = Bank.query.order_by(Bank.name).all()
    categories = Category.query.order_by(Category.name).all()
    return make_response(to_json({
        'banks': banks,
        'categories': categories
    }))

@budgeto_rest.route('/validate-token', methods=['GET'])
@token_required
def validate_token():
    """Will return an error code if the token is invalidated
    Otherwise, return the user information.
    """
    transactions = Transaction.filter(user_id=g.user.user_id).order_by(Transaction.date.desc()).all()
    limits = Limit.get_all(user_id=g.user.user_id)
    return make_response(to_json({
        'user': g.user.to_json,
        'limits': limits,
        'transactions': [t.as_dict() for t in transactions]
    }))

@budgeto_rest.route('/transactions', methods=['GET', 'POST'])
@token_required
def get_transactions():
    # Retrieve user transactions
    if request.method == 'GET':
        transactions = Transaction.filter(user_id=g.user.user_id).order_by(Transaction.date.desc()).all()
        return make_response(to_json(transactions))

    # Allow to insert new transactions
    elif request.method == 'PUT':
        return HttpResponse("Function not implemented yet.", status=418)

    # Allow to edit a transaction
    elif request.method == 'POST':
        return HttpResponse("Function not implemented yet.", status=418)

    # Allow to delete a transaction
    elif request.method == 'DELETE':
        return HttpResponse("Function not implemented yet.", status=418)
