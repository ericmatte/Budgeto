from sqlalchemy import Column
from sqlalchemy import Date
from sqlalchemy import FetchedValue
from sqlalchemy import Float
from sqlalchemy.orm import exc
from sqlalchemy.orm import  relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode, DateTime

from endless.server.base import DeclarativeBase, BaseEntity
from models.category import Category


class Transaction(DeclarativeBase, BaseEntity):
    __tablename__ = 'transaction'

    transaction_id = Column('transaction_id', Integer, primary_key=True)
    user_id = Column('user_id', ForeignKey('user.user_id'))
    bank_id = Column('bank_id', ForeignKey('bank.bank_id'))
    category_id = Column('category_id', ForeignKey('category.category_id'))

    uuid = Column('uuid', Unicode(45))
    description = Column('description', Unicode(256))
    amount = Column('amount', Float)
    date = Column('date', Date)
    upload_time = Column('upload_time', DateTime, server_default=FetchedValue())

    category = relationship(Category)
    user = relationship('User')
    bank = relationship('Bank')

    @classmethod
    def by_user_id(cls, user_id):
        try:
            return cls.query.filter(cls.user_id == user_id).all()
        except exc.NoResultFound:
            return None

    @classmethod
    def by_uuid(cls, uuid, create_if_not_exists):
        try:
            return cls.query.filter(cls.uuid == uuid).one()
        except exc.NoResultFound:
            if create_if_not_exists:
                from endless import db_session
                t = Transaction()
                t.uuid = uuid
                db_session.add(t)
                return t
            else:
                return None

    @classmethod
    def get_all_hierarchical(cls, parent_id=None):
        """Get all the categories in a hierarchical way"""
        categories = {}
        for category in Category.get_all(parent_id=parent_id):
            transactions = cls.get_all(category_id=category.category_id)
            children_categories = cls.get_all_hierarchical(category.category_id)
            transactions_count = len(transactions) + sum([c['count'] for k, c in children_categories.items()])
            categories[category.category_id] = {'category': category,
                                                'count': transactions_count,
                                                'transactions': transactions,
                                                'children': children_categories}
        return categories