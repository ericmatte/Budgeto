import uuid

from sqlalchemy import Column
from sqlalchemy import Date
from sqlalchemy import FetchedValue
from sqlalchemy import Float
from sqlalchemy.orm import exc
from sqlalchemy.orm import  relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode, DateTime

from endless.server.base import DeclarativeBase, BaseEntity
from models import Bank
from models import Category
from models.limit import Limit


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
    def generate_uuid(cls, user_id, bank_id, amount, date, **kargs):
        uuid_string = "%d %d %d %s" % (user_id, bank_id, amount, date.strftime("%d-%m-%Y"))
        return str(uuid.uuid5(uuid.NAMESPACE_DNS, uuid_string))

    @classmethod
    def get_all_hierarchically(cls, user):
        """Get all the categories in a hierarchical way"""
        get_category_transactions = lambda category_id, transactions: [t for t in transactions if t.category_id == category_id]

        def get_children(parent_id, categories, transactions):
            node_categories = {}
            for category in categories:
                if category.parent_id == parent_id:
                    children_categories = get_children(category.category_id, categories, transactions)
                    node_transactions = get_category_transactions(category.category_id, transactions)
                    keywords_count = len(node_transactions) + sum([c['count'] for k, c in children_categories.items()])
                    node_categories[category.category_id] = {'category': category,
                                                             'limit': Limit.get(category=category, user=user),
                                                             'children': children_categories,
                                                             'count': keywords_count,
                                                             'transactions': node_transactions}
            return node_categories

        categories = Category.get_all()
        return get_children(None, categories, user.transactions)

    @classmethod
    def get_all_by_bank(cls, user):
        """Get all the categories in a hierarchical way"""
        banks = [bank.__dict__ for bank in Bank.get_all()]
        transactions = user.transactions

        for bank in banks:
            bank['transactions'] = [t for t in transactions if t.bank_id == bank['bank_id']]
        return banks
