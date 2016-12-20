import os
os.environ['MODE'] = 'DEBUG'

if __name__ == '__main__':
    from endless.server.flask import app
    app.run(debug=True, threaded=True)
