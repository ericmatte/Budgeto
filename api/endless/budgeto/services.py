from flask import request
from werkzeug.exceptions import BadRequestKeyError

from endless import db_session
from endless.budgeto import budgeto_services
from lib.response_handler import HttpResponse, HttpErrorResponse
from models import Bank,  Transaction, User, Category


@budgeto_services.route('/get-transactions', methods=['GET'])
def get_transactions():
    data = request.form
    user = User.by_email(data['email'])
    return HttpResponse(Transaction.by_user_id(user.id))


# Fonctionne si le device envoye seulement les nouvelles transactions
@budgeto_services.route('/set-transactions', methods=['POST'])
def get_transactions():
    try:
        data = request.form
        user = User.by_email(data['email'])
        for transaction in data['transactions']:
            trans = Transaction()
            trans.user_id = user.id
            trans.bank_id = Bank.by_name(transaction['bank']).id
            trans.category_id = Category.by_name(transaction.get('cat', 'Unknown')).id
            trans.description = transaction['desc']
            trans.amount = transaction['amount']
            trans.date = transaction['date']
            db_session.add(trans)

        db_session.commit()
        return HttpResponse('Transactions received!')
    except BadRequestKeyError as e:
        HttpErrorResponse(e)  # Log the error
        return HttpResponse('A field is missing: ' + str(', '.join(e.args)), status=400)