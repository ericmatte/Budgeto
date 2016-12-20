from sqlalchemy import Column
from sqlalchemy.orm import exc
from sqlalchemy.orm import relationship
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity


class Bank(DeclarativeBase, BaseEntity):
    __tablename__ = 'bank'

    bank_id = Column('bank_id', Integer, primary_key=True)

    name = Column('name', Unicode(30))

    transactions = relationship('Transaction', lazy="dynamic")

    @property
    def users(self):
        """Return if the user is a BBI approver or not"""
        return self.query.join('Transaction').join('User').all()

    @classmethod
    def by_name(cls, name):
        try:
            return cls.query.filter(cls.name == name).one()
        except exc.NoResultFound:
            return None
