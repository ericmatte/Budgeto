from endless import app
from server.database import init_database

if __name__ == "__main__":
    init_database()
    app.run(threaded=True)
