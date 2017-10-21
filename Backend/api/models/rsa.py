from sqlalchemy import Column
from sqlalchemy import FetchedValue
from sqlalchemy.orm import relationship
from sqlalchemy.types import Integer, Unicode, DateTime

from endless.server.base import DeclarativeBase, BaseEntity


class Rsa(DeclarativeBase, BaseEntity):
    __tablename__ = 'rsa'

    rsa_id = Column('rsa_id', Integer, primary_key=True)

    public_key = Column('public_key', Unicode(4096))
    creation_date = Column('creation_date', DateTime, server_default=FetchedValue())

    device = relationship('Device')
