import uuid
from datetime import datetime

from flask import g
from flask import json
from flask import request
from werkzeug.exceptions import BadRequestKeyError

from endless.budgeto import budgeto_services
from endless.flask import db_session
from endless.main.services import login_required
from lib.response_handler import HttpResponse, HttpErrorResponse
from models import Bank,  Transaction, User, Category, set_attributes, add_to_db
from models import Keyword
from models.limit import Limit


"""START OF Transaction section"""
@budgeto_services.route('/add-transaction', methods=['POST'])
@login_required
def add_transaction():
    try:
        data = request.form
        t_attributes = {
            'user_id': g.user.user_id,
            'bank_id': int(data['bank']),
            'category_id': int(data['cat']),
            'description': data['desc'] or Category.get(category_id=int(data['cat'])).name,
            'amount': float(data['amount']),
            'date': datetime.strptime(data['date'], '%d-%m-%Y')
        }
        transaction = add_to_db(Transaction(), **t_attributes)
        Keyword.generate_from_description(transaction.description)
        return HttpResponse('Transactions created!', {'transaction': transaction.uuid}, status=201)
    except BadRequestKeyError as e:
        return HttpErrorResponse(e, 'Unable to add the transaction.', status=400)


@budgeto_services.route('/delete-transaction', methods=['POST'])
@login_required
def delete_transaction():
    try:
        transaction = Transaction.get(transaction_id=request.form['id'])
        db_session.delete(transaction)
        db_session.commit()
        return HttpResponse('Transactions deleted.', status=200)
    except BadRequestKeyError as e:
        return HttpErrorResponse(e, 'Unable to delete the transaction.', status=400)
"""END OF Transaction section"""


"""START OF Category section"""
@budgeto_services.route('/save-category-options', methods=['POST'])
@login_required
def save_category_options():
    try:
        data = request.form
        # Setting category limit
        limit_attributes = {
            'user_id': g.user.user_id,
            'category_id': int(data['cat']),
            'value': float(data.get('limit', 0) or 0),
        }

        limit = Limit.get(user_id=g.user.user_id, category_id=limit_attributes['category_id'])
        if data.get('setLimit') and limit_attributes['value'] > 0:
            if limit is None:
                add_to_db(Limit(), **limit_attributes)
            else:
                limit.value = limit_attributes['value']
                db_session.commit()
        else:
            if limit is not None:
                db_session.delete(limit)
                db_session.commit()

        return HttpResponse('Options saved!', status=200)
    except BadRequestKeyError as e:
        return HttpErrorResponse(e, 'Unable to save the options.', status=400)
"""END OF Category section"""


"""START OF Keyword section"""
@budgeto_services.route('/set-keywords', methods=['POST'])
@login_required
def set_keywords():
    data = request.get_json(silent=True)
    for key, values in data['keywords'].items():
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
@login_required
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
@login_required
def delete_keywords():
    ids = request.values['keywordIds'].split(',')
    keywords = Keyword.get_all(Keyword.keyword_id.in_(ids))
    for keyword in keywords:
        keyword.categories = []
        db_session.delete(keyword)
    db_session.commit()
    return HttpResponse('Keywords deleted.')



@budgeto_services.route('/link-keyword-to-category', methods=['POST'])
@login_required
def link_keyword_to_category():
    keyword = Keyword.get(keyword_id=request.values['keywordId'])
    category_id = request.values['categoryId']
    keyword.categories = [Category.get(category_id=category_id)] if category_id != '-1' else []
    db_session.commit()
    return HttpResponse('Keyword {0} assigned to category {1}'.format(keyword.keyword_id, category_id))
"""END OF Category section"""


@budgeto_services.route('/fetch-transactions', methods=['POST'])
@login_required
def fetch_transactions():
    try:
        fetch_transactions_with_db(g.user, json.loads(request.form['transactions']))
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
        t_cat = Category.get(category_id=transaction['cat']) if transaction['cat'].isdigit() else Category.by_name(transaction['cat'])
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