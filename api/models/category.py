from sqlalchemy import Column
from sqlalchemy import Table
from sqlalchemy.orm import relation
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode
from server.base import DeclarativeBase, BaseEntity, metadata

# many-to-many relationship between user and device
category_has_keyword = Table('category_has_keyword', metadata,
    Column('category_id', Integer, ForeignKey('category.category_id', onupdate="CASCADE", ondelete="CASCADE")),
    Column('keyword_id', Integer, ForeignKey('keyword.keyword_id', onupdate="CASCADE", ondelete="CASCADE"))
)


class Category(DeclarativeBase, BaseEntity):
    __tablename__ = 'category'

    category_id = Column('category_id', Integer, primary_key=True)

    name = Column('name', Unicode(30))

    keywords = relation('Keyword', secondary=category_has_keyword, backref='categories')
