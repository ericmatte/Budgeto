from flask import request
from endless.budgeto import budgeto_services
from models import Bank,  Transaction, User, Category


@budgeto_services.route('/get-transactions', methods=['GET'])
def get_transactions():
    return '<b>TRANSACTION COMPLETE</b>'


@budgeto_services.route('/set-transactions', methods=['POST'])
def get_transactions():
    data = request.form
    user = User.by_email(data['email'])
    for transaction in data['transactions']:
        trans = Transaction()
        trans.user_id = user.id
        trans.bank_id = Bank.by_name(transaction['bank']).id
        trans.category_id = Category.by_name(transaction.get('cat', 'Other')).id
        trans.description = transaction['desc']
        trans.amount = transaction['amount']
        trans.date = transaction['date']