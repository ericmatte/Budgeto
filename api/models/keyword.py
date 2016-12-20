from sqlalchemy import Column
from sqlalchemy.orm import relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity


class Keyword(DeclarativeBase, BaseEntity):
    __tablename__ = 'keyword'

    keyword_id = Column('keyword_id', Integer, primary_key=True)
    language_id = Column('language_id', ForeignKey('language.language_id'))

    name = Column('name', Unicode(30))

    language = relationship('Language')
    categories = None  # back_ref from Category
