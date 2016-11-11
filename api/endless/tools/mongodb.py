from pymongo import MongoClient


class MongoDB:
    """
   MongoDB Class to implement stateless operations with
   the MongoDB database. To be used with MongoDBContext.
   """
    def __init__(self, uri, collection):
        self.uri = uri
        self.collection = collection
        self.warehouse = None

    def connect(self):
        """
        Connection to the database.
        """
        self.warehouse = MongoClient(self.uri)
        self.collection = self.warehouse[self.collection]

    def disconnect(self):
        """
        Disconnect from the database.
        """
        self.warehouse.close()

    def find_one_by(self, table, queries):
        """
        Find the first occurrence that match the queries.
        Should be used to search unique data.

        :param table:     The table that contains the data
        :param queries:   A dict with this form -> {key: value, key2: value2}
                          Refer to the MongoDB cheat sheet to learn about queries
                          Ex: {"count": {$gt: 20}} -> the count attribute is greater than 20
        :return:          A JSON object that define the data
        """
        return self.collection[table].find_one(queries)

    def find_by(self, table, queries):
        """
        Find the data that match the queries.

        :param table:     The table that contains the data
        :param queries:   A dict with this form -> {key: values, key2: values2}
                          Refer to the MongoDB cheat sheet to learn about queries
        :return:          A JSON object list that define the data
        """
        return self.collection[table].find(queries)


class MongoDBContext:
    """
    MongoDBContext handle the connection when
    using MongoDB. We use this because we dont
    want to have a continous connection to the
    database (stateless).
    """
    def __init__(self, cr):
        self.cr = cr

    def __enter__(self):
        self.cr.connect()

    def __exit__(self, type, value, traceback):
        self.cr.disconnect()
