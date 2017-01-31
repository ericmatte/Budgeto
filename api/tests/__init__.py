from contextlib import contextmanager
from flask import appcontext_pushed
from flask import g
from endless.flask import app
from models.user import User


@contextmanager
def set_current_user():
    def handler(sender, **kwargs):
        g.user = User.get(user_id=1)
    with appcontext_pushed.connected_to(handler, app):
        with app.app_context():
            yield