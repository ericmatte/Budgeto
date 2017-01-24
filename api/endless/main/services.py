import time
from functools import wraps
from endless.flask import app, db_session
from flask import g
from flask import redirect
from flask import request
from flask import session
from flask import url_for

from lib.response_handler import HttpResponse
from endless.main import main_services
from oauth2client import client, crypt

from models import User, add_to_db, set_attributes


def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if g.user is None:
            return redirect(url_for('main.login', redirect=request.url))
        return f(*args, **kwargs)
    return decorated_function


@main_services.route('/logout', methods=['POST'])
def logout():
    del session['email']
    return HttpResponse('Success logout!')


@main_services.route('/token-signin', methods=['POST'])
def google_token_signin():
    # https://developers.google.com/identity/sign-in/web/backend-auth
    try:
        idinfo = client.verify_id_token(request.values['idtoken'], app.config['CLIENT_ID'])

        if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
            raise crypt.AppIdentityError("Wrong issuer.")
        if idinfo['exp'] <= time.time():
            raise crypt.AppIdentityError("Token expired.")

        update_user(idinfo)
        session['email'] = idinfo['email']
    except crypt.AppIdentityError:
        del session['email']
        return HttpResponse('Client token invalid!', status=401)
    return HttpResponse('User {0} connected!'.format(idinfo['name']))

def update_user(google_user_info):
    """Update user using Google id token. Create a new user if it does not exist."""
    user_info = {'first_name': google_user_info['given_name'],
                 'last_name': google_user_info['family_name'],
                 'picture': google_user_info['picture']}

    user = User.get(google_id=google_user_info['sub'])
    if user is None:
        user = User.get(email=google_user_info['email'])
        user_info['google_id'] = google_user_info['sub']
        if user is None:
            user_info['email'] = google_user_info['email']
            add_to_db(User(), **user_info)
        else:
            set_attributes(user, **user_info)
            db_session.commit()
    else:
        set_attributes(user, **user_info)
        db_session.commit()
