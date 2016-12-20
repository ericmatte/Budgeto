import os

from flask.app import Flask

from endless.server.database import init_db

app = Flask(__name__)
selected_config_file = ('../../../deployment_config/config_{0}.cfg'
                        .format('prod' if os.environ.get('MODE', '') == 'PROD' else 'debug'))
app.config.from_pyfile(selected_config_file)

# Database init
db_session, server = init_db(app.config)
