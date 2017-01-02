from flask import render_template

from endless.budgeto import budgeto


@budgeto.route('/manage-keywords', methods=['GET'])
def home():
    return render_template('home.html')
