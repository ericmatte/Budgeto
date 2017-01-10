import copy

from flask import g
from flask import render_template
from flask import request
from flask import session
from sqlalchemy import and_

from endless.budgeto import budgeto
from models import Category
from models import Keyword
from models import User, Transaction


@budgeto.route('/keywords-categorizer', methods=['GET'])
def keywords_categorizer():
    keywords_tree = Keyword.get_all_hierarchically()
    return render_template('keywords_categorizer.html', title="Keywords Categorizer",
                           keywords_tree=keywords_tree,
                           unassociated_keywords=Keyword.get_all(and_(~Keyword.categories.any(), Keyword.value != None)))


@budgeto.route('/keywords-creator', methods=['GET'])
def keywords_creator():
    remaining_keywords = Keyword.get_all(value=None)
    return render_template('keywords_creator.html', title="Keywords Creator", keywords=remaining_keywords)


@budgeto.route('/transactions', methods=['GET'])
def transactions():
    user = User.get(email=g.email)
    return render_template('transactions.html', title="Transactions",
                           transactions=user.transactions)


@budgeto.route('/budget', methods=['GET'])
def budget():
    transactions_tree = Transaction.get_all_hierarchically(g.email)
    return render_template('budget.html', title="Budget",
                           transactions_tree=clean_empty_leaves_on_tree(transactions_tree))


def clean_empty_leaves_on_tree(tree, count_key='count', children_key='children'):
    new_tree = {}
    for key, item in tree.items():
        if tree[key][count_key] != 0:
            new_tree[key] = copy.copy(item)
            new_tree[key][children_key] = clean_empty_leaves_on_tree(new_tree[key][children_key], count_key, children_key)
    return new_tree