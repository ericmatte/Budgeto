from flask.app import Flask

flask = Flask(__name__)

flask.config.from_pyfile('../../deployment_config/config.cfg')