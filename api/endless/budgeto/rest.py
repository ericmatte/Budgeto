from flask import g
from flask import json
from flask import make_response

from endless.budgeto import budgeto_rest
from endless.main.services import token_required
from lib.response_handler import HttpResponse
from models import Transaction

@budgeto_rest.route('/transactions', methods=['GET'])
@token_required
def get_transactions():
    transactions = Transaction.get_all_by_bank(g.user)
    return make_response(json.dumps(transactions))
