import os
os.environ['MODE'] = 'PROD'

if __name__ == "__main__":
    from endless import app
    app.run(threaded=True)
