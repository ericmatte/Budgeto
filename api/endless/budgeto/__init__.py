from flask.blueprints import Blueprint

budgeto = Blueprint('budgeto', __name__, template_folder='templates')
budgeto_services = Blueprint('budgeto_services', __name__, template_folder='templates')
