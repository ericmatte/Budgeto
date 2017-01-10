import uuid
from datetime import datetime

from flask import json
from flask import request
from werkzeug.exceptions import BadRequestKeyError

from endless.budgeto import budgeto_services
from endless.flask import db_session
from lib.response_handler import HttpResponse, HttpErrorResponse
from models import Bank,  Transaction, User, Category, set_attributes, add_to_db
from models import Keyword


@budgeto_services.route('/set-keywords', methods=['POST'])
def set_keywords():
    json = request.get_json(silent=True)
    for key, values in json['keywords'].items():
        keyword = Keyword.get(keyword_id=key)
        description = keyword.description
        for i in range(len(values)):
            keyword.value = values[i]
            db_session.commit()
            if i != len(values)-1:
                # Creating new keyword for multiple values
                keyword = set_attributes(Keyword(), description=description)
                db_session.add(keyword)
    return HttpResponse('Keywords set!')


@budgeto_services.route('/create-keywords', methods=['POST'])
def create_keywords():
    values = request.values['keyword'].split(',')
    keywords = Keyword.get_all(Keyword.value.in_(values))
    if len(keywords) == 0:
        keywords = [add_to_db(Keyword(), value=v) for v in values]
        return HttpResponse({'keywords': [{'value': k.value, 'id': k.keyword_id} for k in keywords]}, 202)
    else:
        if len(keywords[0].categories) > 0:
            return HttpResponse('Keyword "{0}" already exists in category "{1}"!'
                                .format(keywords[0].value, keywords[0].categories[0].name), 302)
        else:
            return HttpResponse('Keyword "{0}" already exists!'.format(keywords[0].value), 302)


@budgeto_services.route('/delete-keywords', methods=['POST'])
def delete_keywords():
    ids = request.values['keywordIds'].split(',')
    keywords = Keyword.get_all(Keyword.keyword_id.in_(ids))
    for keyword in keywords:
        keyword.categories = []
        db_session.delete(keyword)
    db_session.commit()
    return HttpResponse('Keywords deleted.')



@budgeto_services.route('/link-keyword-to-category', methods=['POST'])
def link_keyword_to_category():
    keyword = Keyword.get(keyword_id=request.values['keywordId'])
    category_id = request.values['categoryId']
    keyword.categories = [Category.get(category_id=category_id)] if category_id != '-1' else []
    db_session.commit()
    return HttpResponse('Keyword {0} assigned to category {1}'.format(keyword.keyword_id, category_id))


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
        return HttpErrorResponse(e, 'A field is missing: ' + str(', '.join(e.args)), status=400)


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
        set_attributes(t, **t_dict)
        db_session.commit()

        generate_keyword_from_description(t_dict['description'])


def generate_keyword_from_description(description=''):
    if description != '':
        if len(Keyword.get_all(Keyword.description == description)) == 0:
            add_to_db(Keyword(), description=description)