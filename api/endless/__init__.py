from flask import g
from flask import redirect
from flask import session
from flask import url_for

from endless.budgeto.controllers import budgeto
from endless.budgeto.services import budgeto_services
from endless.flask import app, db_session
from endless.main.controllers import main
from models.user import User


@app.before_request
def get_current_user():
    session['email'] = 'ericmatte.inbox@gmail.com'
    g.email = session['email']


@app.route('/', methods=['GET'])
def index():
    return redirect(url_for('main.home'))


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


# Blueprints
app.register_blueprint(main, url_prefix='/endless')
app.register_blueprint(budgeto, url_prefix='/budgeto')
app.register_blueprint(budgeto_services, url_prefix='/budgeto')
