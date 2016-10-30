from endless.flask import flask


@flask.route('/', methods=['GET'])
def home():
    return "tbnk"


if __name__ == '__main__':
    flask.run(debug=True, threaded=True)
