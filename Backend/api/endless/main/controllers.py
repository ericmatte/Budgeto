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

@main.route('/login', methods=['GET'])
def login():
    return render_template('login.html', title='Login to Budgeto')