import os
os.environ['MODE'] = 'DEBUG'

from endless.server.flask import app
if __name__ == '__main__':
    app.run(threaded=True)
