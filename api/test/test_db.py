from bson import ObjectId

from endless.flask import flask
from endless.tools.mongodb import MongoDB, MongoDBContext


def test_db():
    db = MongoDB(flask.config["MONGODB_URI"], "Budgeto")
    # We simulate the communication between server/db
    with  MongoDBContext(db):
        bob = db.find_one_by('user', {'email': 'BoblikesAlice@gmail.com'})
        assert bob["_id"] == ObjectId('5816b655a91049131fcfdf99')