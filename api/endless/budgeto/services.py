from endless.budgeto import budgeto_services


@budgeto_services.route('/get-transactions', methods=['GET'])
def get_transactions():
    return '<b>TRANSACTION COMPLETE</b>'
