from flask import render_template
from flask import request

from endless.budgeto import budgeto
from models import Category
from models import Keyword


@budgeto.route('/keywords-creator', methods=['GET'])
def keywords_creator():
    remaining_keywords = Keyword.query.filter(Keyword.value == None).all()
    return render_template('keywords_creator.html', title="Keywords Creator", keywords=remaining_keywords)



@budgeto.route('/keywords-categorizer', methods=['GET'])
def keywords_categorizer():
    a = request
    return render_template('keywords_categorizer.html', title="Keywords Categorizer",
                           categories=Category.get_all_hierarchical(),
                           keywords=Keyword.get_all_by_category())