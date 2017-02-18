import uuid
from datetime import datetime

from flask import g
from flask import json
from flask import request
from werkzeug.exceptions import BadRequestKeyError

from endless.budgeto import budgeto_services
from endless.flask import db_session
from endless.main.services import login_required
from lib.categorizer import Categorizer
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


@budgeto_services.route('/transactions-fetcher', methods=['POST'])
@login_required
def transactions_fetcher():
    truncate = lambda data, max: (data[:max-3] + '...') if len(data) > max-3 else data

    transactions_count = 0
    transactions = json.loads(request.form['transactions'])
    for transaction in transactions:
        try:
            # Getting fields
            attributes = {
                'user': g.user,
                'bank': Bank.get(name=transaction.get('bank')),
                'description': truncate(transaction.get('desc', ''), 256),
                'amount': float(transaction.get('amount', '0').replace(',', '.') or 0),
                'date': datetime.strptime(transaction.get('date'), '%d-%m-%Y')
                        if transaction.get('date') else datetime.today()
            }
            # Fields validation
            if not attributes['description'] or not attributes['bank']:
                continue
            if attributes['date'] > datetime.today():
                attributes['date'] = datetime.today()
            # Trying to find a category
            category = Categorizer.find_category(attributes['description'])
            # Creating transaction
            db_transaction = Transaction.get(**attributes)
            if db_transaction is None:
                db_transaction = add_to_db(Transaction(), **attributes)
            if category is not None:
                db_transaction.category = category
                db_session.commit()
            transactions_count += 1
        except Exception as e:
            pass  # Skip this transaction

    return HttpResponse('Transactions fetched.', {'count': transactions_count}, status=200)
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