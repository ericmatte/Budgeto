from flask import redirect
from flask import render_template
from flask import url_for

from endless.main import main
from endless.main.services import login_required, required_roles


@main.route('/', methods=['GET'])
@login_required
@required_roles('admin')
def home():
    return redirect(url_for('budgeto.budget'))


@main.route('/login', methods=['GET'])
def login():
    return render_template('login.html', title='Login to Budgeto')