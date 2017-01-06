from sqlalchemy import Column
from sqlalchemy import DateTime
from sqlalchemy import FetchedValue
from sqlalchemy import Table
from sqlalchemy.orm import exc
from sqlalchemy.orm import relation
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity, metadata

# many-to-many relationship between user and device
category_has_keyword = Table('category_has_keyword', metadata,
    Column('category_id', Integer, ForeignKey('category.category_id', onupdate="CASCADE", ondelete="CASCADE")),
    Column('keyword_id', Integer, ForeignKey('keyword.keyword_id', onupdate="CASCADE", ondelete="CASCADE"))
)


class Category(DeclarativeBase, BaseEntity):
    __tablename__ = 'category'

    category_id = Column('category_id', Integer, primary_key=True)
    parent_id = Column('parent_id', ForeignKey('category.category_id'))

    name = Column('name', Unicode(45))
    icon = Column('icon', Unicode(45))
    creation_time = Column('creation_time', DateTime, server_default=FetchedValue())
    update_time = Column('update_time', DateTime, server_default=FetchedValue())

    keywords = relation('Keyword', secondary=category_has_keyword, backref='categories')

    @classmethod
    def by_name(cls, name):
        from sqlalchemy import func
        try:
            return cls.query.filter(func.lower(cls.name) == name.lower()).one()
        except exc.NoResultFound:
            try:
                from models import Keyword
                keyword = Keyword.query.filter(func.lower(Keyword.value) == name.lower()).one()

                # TODO: Apply AI categorizer from keywords intelligence here
                return keyword.categories[0] if len(keyword.categories) > 0 else None
            except exc.NoResultFound:
                cls.add_keyword(name)
                return None

    @classmethod
    def get_all_hierarchical(cls, parent_id=None):
        """Get all the categories in a hierarchical way"""
        children_categories = cls.query.filter(cls.parent_id == parent_id).all()
        return {child.category_id: {'category': child, 'children': cls.get_all_hierarchical(child.category_id)}
                for child in children_categories}

    @staticmethod
    def add_keyword(name):
        from endless import db_session
        from models import Keyword
        keyword = Keyword()
        keyword.value = name
        db_session.add(keyword)
        db_session.commit()
