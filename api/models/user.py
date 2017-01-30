from sqlalchemy import Column, func, or_
from sqlalchemy import FetchedValue
from sqlalchemy import Table
from sqlalchemy.orm import exc, relationship, relation
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode, String, DateTime

from endless.server.base import DeclarativeBase, BaseEntity, metadata

# many-to-many relationship between user and device
user_has_device = Table('user_has_device', metadata,
    Column('user_id', Integer, ForeignKey('user.user_id', onupdate="CASCADE", ondelete="CASCADE")),
    Column('device_id', Integer, ForeignKey('device.device_id', onupdate="CASCADE", ondelete="CASCADE"))
)


class User(DeclarativeBase, BaseEntity):
    __tablename__ = 'user'

    user_id = Column('user_id', Integer, primary_key=True)
    language_id = Column('language_id', ForeignKey('language.language_id'))

    first_name = Column('first_name', Unicode(45))
    last_name = Column('last_name', Unicode(45))
    picture = Column('picture', Unicode(256))
    email = Column('email', String(128))
    google_id = Column('google_id', String(30))
    creation_time = Column('creation_time', DateTime, server_default=FetchedValue())
    update_time = Column('update_time', DateTime, server_default=FetchedValue())

    language = relationship('Language')
    transactions = relation('Transaction')
    devices = relation('Device', secondary=user_has_device, backref='users')
    logs = relationship('Log', lazy="dynamic")

    def __repr__(self):
        return ('<User: "%s %s" <%s>>' % (self.first_name, self.last_name, self.email)).encode('utf-8')

    @property
    def full_name(self):
        return self.first_name + ' ' + self.last_name

    @property
    def roles(self):
        """To be changed with real roles"""
        return ['admin', 'user'] if self.user_id in [1,2] else ['user']

    @classmethod
    def apply_filter(cls, query, **kwargs):
        # User ID
        user_id = kwargs.get('user_id', '').strip()
        if user_id:
            query = query.filter(cls.user_id == user_id)

        # User ID
        language_id = kwargs.get('language', '').strip()
        if language_id:
            query = query.filter(cls.language.name == language_id)

        # For search results
        search_value = kwargs.get('search_value')
        if search_value:
            query = query.filter(or_(
                cls.user_id.like("%" + search_value + "%"),
                cls.first_name.like("%" + search_value + "%"),
                cls.last_name.like("%" + search_value + "%"),
                cls.email.like("%" + search_value + "%"),
                func.date_format(cls.activation, '%Y-%m-%d').like("%" + kwargs.get('search_value') + "%"),
            ))

        return query

    def save(self):
        from endless import db_session
        from sqlalchemy import inspect
        from models.log import Log

        logs = Log()
        user_history = inspect(self).attrs

        # Log full name changes
        first_name = (user_history.firstName.history[2] or user_history.firstName.history[1])[0]
        last_name = (user_history.lastName.history[2] or user_history.lastName.history[1])[0]
        if self.firstName != first_name or self.lastName != last_name:
            logs.add_action_log("Edit User: {0}".format(self.userId), "Name",
                                first_name + ' ' + last_name, self.firstName + ' ' + self.lastName)
        db_session.commit()
        logs.save()
