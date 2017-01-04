from flask import render_template

from endless.budgeto import budgeto
from models import Category


@budgeto.route('/keywords-editor', methods=['GET'])
def keywords_editor():
    return render_template('keywords_editor.html', title="Keywords Editor")



@budgeto.route('/keywords-categorizer', methods=['GET'])
def keywords_categorizer():
    categories = Category.get_all_hierarchical()
    return render_template('keywords_categorizer.html', title="Keywords Categorizer", categories=categories)
