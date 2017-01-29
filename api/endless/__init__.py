from functools import wraps

from flask import g
from flask import redirect
from flask import request
from flask import session
from flask import url_for

from endless.budgeto.controllers import budgeto
from endless.budgeto.services import budgeto_services
from endless.flask import app, db_session
from endless.main.services import main_services
from endless.main.controllers import main
from models.user import User

@app.before_request
def get_current_user():
    g.user = User.get(email=session['email']) if session.get('email') else None

    if '127.0.0.1' in request.host:
        # In order for Google Sign in to works
        return redirect(request.url.replace('127.0.0.1', 'localhost'))


@app.route('/', methods=['GET'])
def index():
    return redirect(url_for('budgeto.budget'))


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


# Blueprints
app.register_blueprint(main, url_prefix='/endless')
app.register_blueprint(main_services, url_prefix='/api')
app.register_blueprint(budgeto, url_prefix='/budgeto')
app.register_blueprint(budgeto_services, url_prefix='/budgeto')
