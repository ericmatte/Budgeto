from flask import g
from flask import json
from flask import make_response
from flask import request

from endless.budgeto import budgeto_rest
from endless.main.services import token_required
from lib.response_handler import HttpResponse
from lib.time_calculator import TimeCalculator
from models import Bank
from models import Category
from models import Transaction, to_json


@budgeto_rest.route('/banks', methods=['GET'])
def get_banks():
    banks = Bank.get_all()
    return make_response(to_json(banks))


@budgeto_rest.route('/categories', methods=['GET'])
def get_categories():
    categories = {c.category_id: c for c in Category.get_all()}
    return make_response(to_json(categories))

@budgeto_rest.route('/transactions', methods=['POST'])
@token_required
def get_transactions():
    transactions = Transaction.get_all(user_id=g.user.user_id, bank_id=request.json['bank_id'])
    return make_response(to_json(transactions))
