import os
os.environ['MODE'] = 'PROD'

from endless.flask import app
if __name__ == '__main__':
    app.run(threaded=True)
