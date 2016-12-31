import uuid
from datetime import datetime

from flask import json
from flask import request
from werkzeug.exceptions import BadRequestKeyError

from endless.budgeto import budgeto_services
from endless.server.flask import db_session
from lib.response_handler import HttpResponse, HttpErrorResponse
from models import Bank,  Transaction, User, Category, set_object_attributes


@budgeto_services.route('/get-transactions', methods=['GET'])
def get_transactions():
    user = User.by_email(request.values['email'])
    return HttpResponse(user.transactions)


@budgeto_services.route('/fetch-transactions', methods=['POST'])
def fetch_transactions():
    try:
        data = request.form
        user = User.by_email(data['email'])
        fetch_transactions_with_db(user, json.loads(data['transactions']))

        return HttpResponse('Transactions received!')
    except BadRequestKeyError as e:
        HttpErrorResponse(e)  # Log the error
        return HttpResponse('A field is missing: ' + str(', '.join(e.args)), status=400)

def fetch_transactions_with_db(user, transactions):
    for transaction in transactions:
        t_dict = {
            'user_id': user.user_id,
            'bank_id': Bank.by_name(transaction['bank']).bank_id,
            'description': transaction['desc'],
            'amount': float(transaction['amount']),
            'date': datetime.strptime(transaction['date'], '%d-%m-%Y')
        }

        # If a category is found for this transaction, get its id
        t_cat = Category.by_name(transaction['cat'])
        if t_cat is not None:
            t_dict['category_id'] = t_cat.category_id

        # Each transactions must have a unique uuid
        uuid_string = "%d %d %d %s" % (t_dict['user_id'], t_dict['bank_id'], t_dict['amount'], transaction['date'])
        transaction_uuid = str(uuid.uuid5(uuid.NAMESPACE_DNS, uuid_string))

        t = Transaction.by_uuid(transaction_uuid, True)
        set_object_attributes(t, t_dict)
        db_session.commit()

