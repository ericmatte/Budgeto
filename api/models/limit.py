from sqlalchemy import Column
from sqlalchemy import Float
from sqlalchemy.orm import relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer

from endless.server.base import DeclarativeBase, BaseEntity

class Limit(DeclarativeBase, BaseEntity):
    __tablename__ = 'limit'

    limit_id = Column('limit_id', Integer, primary_key=True)
    user_id = Column('user_id', ForeignKey('user.user_id'))
    category_id = Column('category_id', ForeignKey('category.category_id'))

    value = Column('value', Float)

    user = relationship('User')
    category = relationship('Category')
