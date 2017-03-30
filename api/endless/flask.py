import os

from flask.app import Flask

from endless.server.database import init_db

app = Flask(__name__)
selected_config_file = ('../../deployment_config/budgeto/config_{0}.cfg'
                        .format('prod' if os.environ.get('MODE', '') == 'PROD' else 'debug'))
app.config["PROJECT_ROOT"] = os.path.abspath(os.path.dirname(__file__))
app.config.from_pyfile(selected_config_file)
app.secret_key = 'f3c82c4584527c7ba650b18c38237cca'

# Database init
db_session, server = init_db(app.config)
