from endless.flask import app

if __name__ == '__main__':
    app.config['DEBUG'] = True
    app.run(debug=True, threaded=True)
