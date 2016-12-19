from contextlib import contextmanager
from flask.globals import g
from flask.signals import appcontext_pushed
import pytest

from endless import app
from server.database import init_database


@pytest.fixture
def client(request):
    app.config['TESTING'] = True
    init_database()

    def teardown():
        pass
    request.addfinalizer(teardown)
    return app.test_client()


@contextmanager
def set_current_user():
    def handler(sender, **kwargs):
        g.user = 'ericmatte.inbox@gmail.com'
    with appcontext_pushed.connected_to(handler, app):
        yield