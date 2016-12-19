from operator import and_
from flask import g
from sqlalchemy import Column
from sqlalchemy import Text
from sqlalchemy.orm import exc, relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, DateTime

from models.language import Language
from server.base import DeclarativeBase, BaseEntity


class Translation(DeclarativeBase, BaseEntity):
    __tablename__ = 'translation'

    user_id = Column('translation_id', Integer, primary_key=True)
    language_id = Column('language_id', ForeignKey('language.language_id'))

    value = Column('value', Text)
    translated_value = Column('translated_value', Text)
    update_time = Column('update_time', DateTime)

    language = relationship(Language)

    @classmethod
    def _(cls, value):
        """Translation function for the API"""
        try:
            translation = cls.query.filter(and_(cls.value == value, cls.language_id == g.language.language_id)).one()
            return translation.translated_value
        except exc.NoResultFound:
            return value
