from contextlib import contextmanager
from flask import appcontext_pushed
from flask import g
from endless.flask import app
from models import add_to_db
from models.user import User


@contextmanager
def set_current_user():
    test_user = add_to_db(User(), first_name="Test", last_name="User", email="test.user@gmail.com")
    def handler(sender, **kwargs):
        g.user = test_user  # User.get(user_id=1)
    with appcontext_pushed.connected_to(handler, app):
        with app.app_context():
            yield