from sqlalchemy import Column
from sqlalchemy import DateTime
from sqlalchemy import FetchedValue
from sqlalchemy import and_
from sqlalchemy.orm import relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity


class Keyword(DeclarativeBase, BaseEntity):
    __tablename__ = 'keyword'

    keyword_id = Column('keyword_id', Integer, primary_key=True)
    language_id = Column('language_id', ForeignKey('language.language_id'))

    value = Column('value', Unicode(256))
    description = Column('description', Unicode(256))
    creation_time = Column('creation_time', DateTime, server_default=FetchedValue())
    update_time = Column('update_time', DateTime, server_default=FetchedValue())

    language = relationship('Language')
    categories = None  # back_ref from Category

    @classmethod
    def get_all_by_category(cls):
        """Get all the categories in a hierarchical way"""
        from models import Category
        c_dict = {c.category_id: [] for c in Category.get_all()}
        c_dict[-1] = []
        for k in cls.get_all(and_(cls.value != None, cls.value != '')):
            if len(k.categories) > 0:
                for c in k.categories:
                    c_dict[c.category_id].append(k)
            else:
                c_dict[-1].append(k)
        return c_dict
