from flask import Flask
from flask import g
from sshtunnel import SSHTunnelForwarder

from models.user import User
from endless.budgeto.services import budgeto_services
from endless.main.controllers import main
from server import database

# Flask app init
app = Flask(__name__)
app.config.from_pyfile('../../deployment_config/config.cfg')

# Database init
database.init_db(app.config['DB_CONNECTION_STRING'], app.config)
db_session = database.get_session()


@app.before_request
def get_current_user():
    g.user = getattr(g, 'user', None)
    user = User.by_email('email')
    # db_session.commit()


@app.route('/', methods=['GET'])
def index():
    return 'Endless API'


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


# Blueprints
app.register_blueprint(main, url_prefix='/endless')
app.register_blueprint(budgeto_services, url_prefix='/budgeto')
