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
    pictures = [{'caption': 'Angel Girl',       'size': '1280x1920', 'filename': 'Angel.jpg'},
                {'caption': 'Backyard',         'size': '2340x1080', 'filename': 'Backyard.jpg'},
                {'caption': 'Blue Moon',        'size': '1920x1248', 'filename': 'Blue-Moon.jpg'},
                {'caption': 'The Little Thing', 'size': '2243x1080', 'filename': 'Boubou.jpg'},
                {'caption': 'The Daisy Flower', 'size': '3132x1080', 'filename': 'Daisy.jpg'},
                {'caption': 'Green Fern',       'size': '1920x1280', 'filename': 'Fern.jpg'},
                {'caption': 'Flower',           'size': '3132x1080', 'filename': 'Flower.jpg'},
                {'caption': 'Flying Girl',      'size': '1920x1280', 'filename': 'Flying-Girl.jpg'},
                {'caption': 'Flying Stars',     'size': '1920x1280', 'filename': 'Flying-Stars.jpg'},
                {'caption': 'Graffiti',         'size': '1920x1421', 'filename': 'Graffiti.jpg'},
                {'caption': 'Gravel Road',      'size': '3132x1080', 'filename': 'Gravel-Road.jpg'},
                {'caption': 'Journey',          'size': '1920x1440', 'filename': 'Journey.jpg'},
                {'caption': 'Jump!',            'size': '2569x1080', 'filename': 'Jump!.jpg'},
                {'caption': 'Lady of Leaves',   'size': '1920x1280', 'filename': 'Lady-of-Leaves.jpg'},
                {'caption': 'Lantern',          'size': '1920x1395', 'filename': 'Lantern.jpg'},
                {'caption': 'Leave',            'size': '3132x1080', 'filename': 'Leave.jpg'},
                {'caption': 'Lumina',           'size': '1920x1183', 'filename': 'Lumina.jpg'},
                {'caption': 'SharinganX',       'size': '1920x1280', 'filename': 'SharinganX.jpg'},
                {'caption': 'The Red Chapel',   'size': '1920x1190', 'filename': 'The-Red-Chapel.jpg'},
                {'caption': 'Tunnel',           'size': '3099x1368', 'filename': 'Tunnel.jpg'},
                {'caption': 'Turtle',           'size': '1920x1280', 'filename': 'Turtle.jpg'},
                {'caption': 'White Flower',     'size': '1920x1440', 'filename': 'White-Flower.jpg'},
                {'caption': 'Wooden Bridge',    'size': '3132x1080', 'filename': 'Wooden-Bridge.jpg'},]
    for picture in pictures:
        picture['url'] = '/static/portfolio/pictures/' + picture['filename']
        picture['thumbnail'] = '/static/portfolio/pictures/thumbnails/' + picture['filename']

    projects = [{'title': 'Budgeto', 'description': "Budgeto is a simple webapp that easily help you manage your money.", 'url': 'http://ericmatte.me/budgeto/'},
                {'title': 'SpirePlane', 'description': "A powerful web browser made in VB.NET. This is one of my first big project. I have coded Spireplane when I was 15 years old. It was based on the firefox engine, and had a ton of customisation settings."},
                {'title': 'Découvrir Sherbrooke [HackSherbrooke]', 'description': "For the hacketon HackSherbrooke 2016, my team and I made a simple application that helps new immigrants find great activities to do in the region. We've won the \"Prix coup de coeur\" for this occasion."},
                {'title': 'Air Guitar Hero', 'description': 'Air Guitar Hero is a game similar to guitar hero. The main difference: The player plays without a guitar! Instead, we used gloves with conductive contacts to detect the player gestures.'},]

    return render_template('portfolio.html',
                           title="Eric Matte - Portfolio",
                           description="This is my personal portfolio. It contains everything that I am very proud to share from what I\'ve done so far.",
                           image='/static/portfolio/my-icon.png',
                           pictures=pictures,
                           projects=projects,
                           color='#00897b',
                           now=datetime.utcnow())


@main.route('/login', methods=['GET'])
def login():
    return render_template('login.html', title='Login to Budgeto')