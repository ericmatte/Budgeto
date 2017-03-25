from datetime import datetime

from flask import redirect
from flask import render_template
from flask import url_for

from endless.main import main
from endless.main.services import login_required, required_roles


@main.route('/', methods=['GET'])
def index():
    return redirect(url_for('budgeto.promotion'))


@main.route('/portfolio', methods=['GET'])
def portfolio():
    pictures = [{'caption': 'Angel Girl', 'url': '/static/portfolio/pictures/Angel.jpg'},
                {'caption': 'Blue Moon', 'url': '/static/portfolio/pictures/Blue-Moon.jpg'},
                {'caption': 'The Little Thing', 'url': '/static/portfolio/pictures/Boubou.jpg'},
                {'caption': 'Green Fern', 'url': '/static/portfolio/pictures/Fern.jpg'},
                {'caption': 'Flying Girl', 'url': '/static/portfolio/pictures/Flying-Girl.jpg'},
                {'caption': 'Flying Stars', 'url': '/static/portfolio/pictures/Flying-Stars.jpg'},
                {'caption': 'Grafiti', 'url': '/static/portfolio/pictures/Grafiti.jpg'},
                {'caption': 'Journey', 'url': '/static/portfolio/pictures/Journey.jpg'},
                {'caption': 'Lady of Leaves', 'url': '/static/portfolio/pictures/Lady-of-Leaves.jpg'},
                {'caption': 'Lantern', 'url': '/static/portfolio/pictures/Lantern.jpg'},
                {'caption': 'Lumina', 'url': '/static/portfolio/pictures/Lumina.jpg'},
                {'caption': 'SharinganX', 'url': '/static/portfolio/pictures/SharinganX.jpg'},
                {'caption': 'The Red Chapel', 'url': '/static/portfolio/pictures/The-Red-Chapel.jpg'},
                {'caption': 'Tunnel', 'url': '/static/portfolio/pictures/Tunnel.jpg'},
                {'caption': 'Turtle', 'url': '/static/portfolio/pictures/Turtle.jpg'},
                {'caption': 'White Flower', 'url': '/static/portfolio/pictures/White-Flower.jpg'},]
    return render_template('portfolio.html',
                           title="Eric Matte - Portfolio",
                           description="This is my personal portfolio. It contains everything that I am very proud to share from what I\'ve done so far.",
                           image='/static/portfolio/my-icon.png',
                           pictures=pictures,
                           color='#00897b',
                           now=datetime.utcnow())


@main.route('/login', methods=['GET'])
def login():
    return render_template('login.html', title='Login to Budgeto')