import codecs
import json
import os
from datetime import datetime
from os import listdir
from os.path import isfile, join
from flask import redirect
from flask import render_template
from flask import url_for
from endless.main import main
from endless.flask import app



@main.route('/', methods=['GET'])
def index():
    return redirect(url_for('budgeto.promotion'))


@main.route('/portfolio', methods=['GET'])
def portfolio():
    root = app.config["PROJECT_ROOT"]
    data = json.load(codecs.open(root + '/static/portfolio/data.json', 'r', 'utf-8-sig'))

    for i in range(len(data['pictures'])):
        data['pictures'][i]["url"] = "/static/portfolio/pictures/" + data['pictures'][i]["filename"]
        data['pictures'][i]["thumbnail"] = "/static/portfolio/pictures/thumbnails/" + data['pictures'][i]["filename"]

    for i in range(len(data['projects'])):
        data['projects'][i]['id'] = 'project-' + str(i)
        img_path = data['projects'][i].get('images_path')
        if img_path:
            data['projects'][i]['images'] = [img_path+f for f in listdir(root + img_path) if isfile(join(root +img_path, f))]

    return render_template('portfolio.html',
                           title="Eric Matte - Portfolio",
                           description="This is my personal portfolio. It contains everything that I am very proud to share from what I\'ve done so far.",
                           image='/static/portfolio/my-icon.png',
                           data=data,
                           color='#034b7b',
                           now=datetime.utcnow())


@main.route('/login', methods=['GET'])
def login():
    return render_template('login.html', title='Login to Budgeto')