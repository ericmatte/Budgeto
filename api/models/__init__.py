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

def set_object_attributes(sql_object, dict):
    for key, value in dict.items():
        setattr(sql_object, key, value)
