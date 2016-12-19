from endless.flask import app
from flask import g

from models.user import User
from endless.budgeto.services import budgeto_services
from endless.main.controllers import main

# Flask app init
# app = Flask(__name__)
# app.config.from_pyfile('../../deployment_config/config.cfg')


@app.before_request
def get_current_user():
    g.user = getattr(g, 'user', None)
    user = User.by_email('email')
    # db_session.commit()


@app.route('/', methods=['GET'])
def index():
    return 'Endless API'


# Blueprints
app.register_blueprint(main, url_prefix='/endless')
app.register_blueprint(budgeto_services, url_prefix='/budgeto')
