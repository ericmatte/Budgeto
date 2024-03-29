from sqlalchemy import Column
from sqlalchemy.orm import exc
from sqlalchemy.orm import relationship
from sqlalchemy.types import Integer, Unicode

from endless.server.base import DeclarativeBase, BaseEntity


class Bank(DeclarativeBase, BaseEntity):
    __tablename__ = 'bank'

    bank_id = Column('bank_id', Integer, primary_key=True)

    name = Column('name', Unicode(30))
    color = Column('color', Unicode(6))

    transactions = relationship('Transaction', lazy="dynamic")

    @property
    def users(self):
        return self.query.join('Transaction').join('User').all()
