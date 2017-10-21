from lib.alchemy_encoder import AlchemyEncoder
from .bank import Bank
from .category import Category
from .device import Device
from .keyword import Keyword
from .language import Language
from .log import Log
from .rsa import Rsa
from .transaction import Transaction
from .translation import Translation
from .user import User
from flask import json


def add_to_db(sql_object, **attributes):
    from endless import db_session
    set_attributes(sql_object, **attributes)
    db_session.add(sql_object)
    db_session.commit()
    return sql_object


def set_attributes(sql_object, **attributes):
    for key, value in attributes.items():
        setattr(sql_object, key, value)
    return sql_object


def to_json(sql_object, to_string=True):
    j = json.dumps(sql_object, cls=AlchemyEncoder)
    return j if to_string else json.loads(j)