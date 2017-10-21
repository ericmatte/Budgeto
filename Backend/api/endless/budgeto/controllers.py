import copy

from datetime import date
from flask import g
from flask import render_template
from sqlalchemy import and_

from endless.budgeto import budgeto
from endless.main.services import login_required, required_roles
from models import Bank
from models import Category
from models import Keyword
from models import Transaction
from models import User



"""START OF Admin section"""
# All route in this section must be set as @login_required and @required_roles('admin')
@budgeto.route('/users', methods=['GET'])
@login_required
@required_roles('admin')
def users():
    return render_template('users.html', title='Users',
                           users=User.get_all())


@budgeto.route('/keywords-categorizer', methods=['GET'])
@login_required
@required_roles('admin')
def keywords_categorizer():
    keywords_tree = Keyword.get_all_hierarchically()
    return render_template('keywords_categorizer.html', title="Keywords Categorizer",
                           keywords_tree=keywords_tree,
                           unassociated_keywords=Keyword.get_all(and_(~Keyword.categories.any(), Keyword.value != None)))


@budgeto.route('/keywords-creator', methods=['GET'])
@login_required
@required_roles('admin')
def keywords_creator():
    remaining_keywords = Keyword.get_all(value=None)
    return render_template('keywords_creator.html', title="Keywords Creator", keywords=remaining_keywords)
"""END OF Admin section"""