from flask import g
from datetime import datetime

from endless import db_session
from models.user import User
from models.log import Log
from endless import app


class Logger:
    def __init__(self):
        self.log_portal_actions = []
        self.log_portal_actions_id = []

    def save(self):
        if not app.config.get('TESTING'):
            for log_portal_actions in self.log_portal_actions:
                db_session.add(log_portal_actions)
            db_session.commit()

    def add_user_log(self, action, description, old_value, new_value):
        log = Log()
        log.id_user = User.byEmail(g.user).userId
        log.action = action
        log.description = description
        log.old_value = old_value
        log.new_value = new_value
        log.date = datetime.now()

        self.log_portal_actions.append(log)
        return self

    # To be done with "LogPortalLogin" model
    def add_login_log(self):
        if not flask.config.get('TESTING'):
            pass
        return self

    # To be done with "LogPortalUse" model
    def add_use_login(self):
        if not flask.config.get('TESTING'):
            pass
        return self
