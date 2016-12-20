from sqlalchemy import Column
from sqlalchemy import DateTime
from sqlalchemy.orm import relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity


class Device(DeclarativeBase, BaseEntity):
    __tablename__ = 'device'

    device_id = Column('device_id', Integer, primary_key=True)
    rsa_id = Column('rsa_id', ForeignKey('rsa.rsa_id'))

    name = Column('name', Unicode(128))
    model = Column('model', Unicode(128))
    version = Column('version', Unicode(32))
    mac_address = Column('mac_address', Unicode(32))
    update_time = Column('update_time', DateTime)

    rsa = relationship('Rsa')
    users = None  # back_ref from User
