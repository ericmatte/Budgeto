from flask.blueprints import Blueprint

main = Blueprint('main', __name__, template_folder='templates')
main_services = Blueprint('main_services', __name__, template_folder='templates')
