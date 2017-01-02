from flask import render_template
from endless.main import main


@main.route('/', methods=['GET'])
def main_layout():
    return render_template('main_layout.html')


@main.route('/home', methods=['GET'])
def home():
    return render_template('home.html')
