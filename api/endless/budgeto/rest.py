from flask import g
from flask import make_response
from flask import request
from endless.budgeto import budgeto_rest
from endless.budgeto.backend import get_transaction_attributes
from endless.main.services import token_required
from lib.response_handler import HttpResponse, HttpErrorResponse
from models import Bank, add_to_db, set_attributes
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

@budgeto_rest.route('/transactions', methods=['GET', 'POST', 'PUT'])
@token_required
def transactions():
    # Retrieve user transactions
    if request.method == 'GET':
        transactions = Transaction.filter(user_id=g.user.user_id).order_by(Transaction.date.desc()).all()
        return make_response(to_json(transactions))

    elif request.method in ['PUT', 'POST']:
        data = request.get_json()
        # Allow to insert new transactions
        if request.method == 'PUT':
            try:
                attributes = get_transaction_attributes(data)
                transaction = add_to_db(Transaction(), **attributes)
                return HttpResponse(transaction.as_dict(), status=201)
            except Exception as e:
                return HttpErrorResponse(e, 'Unable to add the transaction. Please try again later.', status=400)

        # Allow to edit a transaction
        elif request.method == 'POST':
            transaction = Transaction.get(user_id=g.user.user_id, transaction_id=data['transaction_id'])
            if transaction is not None:
                try:
                    from endless import db_session
                    attributes = get_transaction_attributes(data)
                    set_attributes(transaction, **attributes)
                    db_session.commit()
                    return HttpResponse(transaction.as_dict(), status=201)
                except Exception as e:
                    return HttpErrorResponse(e, 'Unable to add the transaction. Please try again later.', status=400)
            else:
                return HttpResponse("Unable to edit the transaction. Please try again later.", status=500)


# Allow to delete a transaction
@budgeto_rest.route('/transactions/delete/<transaction_id>', methods=['DELETE'])
@token_required
def delete_transaction(transaction_id):
    transaction = Transaction.get(user_id=g.user.user_id, transaction_id=transaction_id)
    if transaction is not None:
        from endless import db_session
        db_session.delete(transaction)
        db_session.commit()
        return HttpResponse("The transaction #{id} has been deleted.".format(id=transaction), status=200)
    else:
        return HttpResponse("Unable to delete the transaction. Please try again later.", status=500)
