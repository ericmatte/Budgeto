from flask import g
from flask import redirect
from flask import url_for

from endless.budgeto.controllers import budgeto
from endless.budgeto.services import budgeto_services
from endless.main.controllers import main
from endless.server.flask import app, db_session
from models.user import User


@app.before_request
def get_current_user():
    g.user = getattr(g, 'user', None)
    user = User.by_email('email')
    db_session.commit()


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
