from flask import render_template
from endless.main import main


@main.route('/', methods=['GET'])
def home():
    return render_template('home.html', title='Main Admin Panel')