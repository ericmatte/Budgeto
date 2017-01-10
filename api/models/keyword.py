from sqlalchemy import Column
from sqlalchemy import DateTime
from sqlalchemy import FetchedValue
from sqlalchemy import and_
from sqlalchemy.orm import relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity
from models import Category


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

    @classmethod
    def get_all_hierarchically(cls):
        """Get all the categories in a hierarchical way"""
        def get_category_keywords(category, keywords):
            return [k for k in keywords if category in k.categories]

        def get_children(parent_id, categories, keywords):
            node_categories = {}
            for category in categories:
                if category.parent_id == parent_id:
                    children_categories = get_children(category.category_id, categories, keywords)
                    node_keywords = get_category_keywords(category, keywords)
                    keywords_count = len(node_keywords) + sum([c['count'] for k, c in children_categories.items()])
                    node_categories[category.category_id] = {'category': category,
                                                             'children': children_categories,
                                                             'count': keywords_count,
                                                             'keywords': node_keywords}
            return node_categories

        categories = Category.get_all()
        keywords = Keyword.get_all()
        return get_children(None, categories, keywords)
