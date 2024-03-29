from sqlalchemy import Column
from sqlalchemy import DateTime
from sqlalchemy import FetchedValue
from sqlalchemy import ForeignKey
from sqlalchemy import Text
from sqlalchemy.orm import relationship
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity


class Log(DeclarativeBase, BaseEntity):
    __tablename__ = 'log'

    log_id = Column('log_id', Integer, primary_key=True)
    user_id = Column('user_id', ForeignKey('user.user_id'))

    action = Column('action', Unicode(128))
    description = Column('description', Unicode(256))
    old_value = Column('old_value', Text)
    new_value = Column('new_value', Text)
    date = Column('date', DateTime, server_default=FetchedValue())

    user = relationship('User')
