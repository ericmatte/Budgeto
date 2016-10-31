from pymongo import MongoClient


class MongoDB:
    def __init__(self, uri, db):
        self.uri = uri
        self.db = db
        self.warehouse = None

    def connect(self):
        self.warehouse = MongoClient(self.uri)
        self.db = self.warehouse[self.db]

    def disconnect(self):
        self.warehouse.close()


class MongoDBContext:
    def __init__(self, cr):
        self.cr = cr

    def __enter__(self):
        self.cr.connect()

    def __exit__(self, type, value, traceback):
        self.cr.disconnect()
