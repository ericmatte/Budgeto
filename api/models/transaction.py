from sqlalchemy import Column
from sqlalchemy import Float
from sqlalchemy.orm import  relationship
from sqlalchemy.schema import ForeignKey
from sqlalchemy.types import Integer, Unicode, DateTime

from models.bank import Bank
from models.category import Category
from server.base import DeclarativeBase, BaseEntity


class Transaction(DeclarativeBase, BaseEntity):
    __tablename__ = 'transaction'

    transaction_id = Column('transaction_id', Integer, primary_key=True)
    user_id = Column('user_id', ForeignKey('user.user_id'))
    bank_id = Column('bank_id', ForeignKey('bank.bank_id'))
    category_id = Column('category_id', ForeignKey('category.category_id'))

    description = Column('description', Unicode(256))
    amount = Column('amount', Float)
    date = Column('date', DateTime)
    upload_time = Column('upload_time', DateTime)

    category = relationship(Category)
    user = relationship('User')
    bank = relationship(Bank)
