import os
import os.path
import sys
import json
import logging
import tornado.websocket
import tornado.web
import tornado.ioloop
import pandas as pd
from datetime import datetime

sys.path.insert(1, os.path.join(os.path.dirname(__file__), '..'))
from perspective import Table, PerspectiveManager

'''
Import the Table and PerspectiveManager classes.

A Perspective `Table` is instantiated either with data or a `schema`.

The `PerspectiveManager` class handles incoming messages from the client Perspective through a WebSocket connection.
'''


class DateTimeEncoder(json.JSONEncoder):
    '''Create a custom JSON encoder that allows serialization of datetime and date objects.'''

    def default(self, obj):
        if isinstance(obj, datetime):
            # Convert to milliseconds - perspective.js expects millisecond timestamps, but python generates them in seconds.
            return obj.timestamp() * 1000
        return super(DateTimeEncoder, self).default(obj)


class MainHandler(tornado.web.RequestHandler):

    def set_default_headers(self):
        self.set_header("Access-Control-Allow-Origin", "*")
        self.set_header("Access-Control-Allow-Headers", "x-requested-with")
        self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS')

    def get(self):
        self.render("perspective_tornado_client.html")


class SimpleWebSocket(tornado.websocket.WebSocketHandler):

    def check_origin(self, origin):
        return True

    def on_message(self, message):
        '''When the websocket receives a message, send it to the `process` method of the `PerspectiveManager` with a reference to the `post` callback.'''
        if message == "heartbeat":
            return
        message = json.loads(message)
        MANAGER.process(message, self.post)

    def post(self, message):
        '''When `post` is called by `PerspectiveManager`, serialize the data to JSON and send it to the client.'''
        message = json.dumps(message, cls=DateTimeEncoder)
        self.write_message(message)


def make_app():
    return tornado.web.Application([
        (r"/", MainHandler),
        # create a websocket endpoint that the client Javascript can access
        (r"/websocket", SimpleWebSocket)
    ])


if __name__ == "__main__":
    '''Create an instance of the `PerspectiveManager`.

    The manager instance tracks tables and views, manages method calls on them, and parses messages from the client.
    '''
    MANAGER = PerspectiveManager()

    '''Perspective can load data in row, column, and dataframe format.

        - Row format (list[dict{string:value}]): [{"column_1": 1, "column_2": "abc", "column_3": True, "column_4": datetime.now(), "column_5": date.today()}]
            * Each element in the list is a dict, which represents a row of data.
        - Column format (dict{string: list}): {"column": [1, 2, 3]}
            * The keys of the dict are string column names, and the values are lists that contain the value for each row.
            * Numpy arrays can also be used in this format, i.e. {"a": numpy.arange(100)}
        - DataFrame (pandas.DataFrame): Perspective has full support for dataframe loading, updating, and editing.

    For this example, we'll load a sample dataframe from CSV, and provide it to the Table.
    '''
    tbl = Table(pd.read_csv("superstore.csv"))

    '''Once the Table is created, pass it to the manager instance with a name.

    Make sure that the name here is used in the client HTML when we call `open_table`.

    Once the manager has the table, commands from the client will be tracked and applied.
    '''
    MANAGER.host_table("data_source_one", tbl)

    # start the Tornado server
    app = make_app()
    app.listen(8888)
    logging.critical("Listending on http://localhost:8888")
    loop = tornado.ioloop.IOLoop.current()
    loop.start()
