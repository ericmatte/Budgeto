from endless.budgeto.services import budgeto_services
from endless.main.controllers import main
from endless.flask import flask as app

# Blueprints
app.register_blueprint(main, url_prefix='/endless')
app.register_blueprint(budgeto_services, url_prefix='/budgeto')
