from sqlalchemy import Column
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity


class Language(DeclarativeBase, BaseEntity):
    __tablename__ = 'language'

    language_id = Column('language_id', Integer, primary_key=True)

    iso_code = Column('iso_code', Unicode(5))
    name = Column('name', Unicode(45))
