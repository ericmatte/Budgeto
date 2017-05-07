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


@budgeto_rest.route('/fetch-initial-data', methods=['GET'])
def fetch_initial_data():
    banks = Bank.get_all()
    categories = Category.get_all() # {c.category_id: c for c in Category.get_all()}
    return make_response(to_json({
        'banks': banks,
        'categories': categories
    }))

@budgeto_rest.route('/validate-token', methods=['GET'])
@token_required
def validate_token():
    """Will return an error code if the token is invalidated"""
    return make_response(json.dumps(g.user.to_json))

@budgeto_rest.route('/transactions', methods=['GET', 'POST'])
@token_required
def get_transactions():
    if request.method == 'GET':
        transactions = Transaction.get_all(user_id=g.user.user_id)
        return make_response(to_json(transactions))


    elif request.method == 'POST':
        transactions = Transaction.get_all(user_id=g.user.user_id, bank_id=request.json['bank_id'])
        return make_response(to_json(transactions))
