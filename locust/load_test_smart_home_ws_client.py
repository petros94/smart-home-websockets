from __future__ import absolute_import
from __future__ import unicode_literals
from __future__ import print_function

import string
import gevent
from locust.exception import StopLocust

from websocket import create_connection
import stomper
import uuid
from locust import HttpLocust, TaskSet, task, between, InterruptTaskSet
from locust.events import request_success, request_failure

from base64 import b64encode
import logging
import time
import datetime
import json
import random
import csv

USER_CREDENTIALS = None


class LDSTaskSet(TaskSet):
    def __init__(self, parent):
        super().__init__(parent)

        self.instanceId = None
        self.ws = None
        self.credentials = dict()
        self.init_time = None

    def login(self):
        credentials = self.credentials
        self.instanceId = credentials['username']
        host = self.locust.host

        """Create random credentials"""
        user_pass = credentials['username'] + ":" + credentials['password']
        encoded_user_pass = b64encode(user_pass.encode('ascii')).decode("utf-8")

        """Create connection"""
        init_time = time.time()
        ws_url = "ws://" + host + "/device-management-service/websocket"
        ws = create_connection(ws_url, header={"Authorization": "Basic " + str(encoded_user_pass)})

        con = stomper.connect(credentials["username"], credentials["password"], host=ws_url, heartbeats=(30000, 30000))
        ws.send(con)

        res = ws.recv()
        self.handle_message(str(res), init_time=init_time)

        """subscribe"""
        init_time = time.time()
        sub = stomper.subscribe("/user/queue/device", 0, ack='auto')
        ws.send(sub)

        res = ws.recv()
        self.handle_message(str(res), init_time=init_time)

        return ws

    def on_start(self):
        if len(USER_CREDENTIALS) > 0:
            username, password = USER_CREDENTIALS.pop()
            self.credentials = {"username": username, "password": password}

    def on_quit(self):
        self.ws.close()

    def handle_message(self, msg, **kwargs):
        init_time = kwargs.get('init_time', time.time())

        if "CONNECTED" in msg:
            logging.info("%s: Connection initiated", self.instanceId)
            request_success.fire(
                request_type='WebSocket Init',
                name='Init websocket connection',
                response_time=(time.time() - init_time) * 1000,
                response_length=len(msg)
            )
        elif "ERROR" in msg:
            logging.warning("%s: Received error: \n %s", self.instanceId, str(msg))
            request_failure.fire(
                request_type='WebSocket Error',
                name='Websocket server Error',
                response_time=(time.time() - init_time) * 1000,
                response_length=len(msg),
                exception=InterruptTaskSet()
            )
            raise StopLocust()
        elif "MESSAGE" in msg:
            logging.info("%s: Message received: %s", self.instanceId, str(msg))
            message = str(msg)
            idx = message.find("{")
            content = message[idx:-1]

            json_msg = json.loads(content)
            res = stomper.send("/app/device",
                               '{ "time": "' + datetime.datetime.now().isoformat() + '",' +
                               '"status": "OK",' +
                               '"id": "' + json_msg["id"] + '",' +
                               '"data": ' + str([1, 0, -1, 0]) +
                               '}',
                               content_type="application/json")
            self.ws.send(res)
            request_success.fire(
                request_type='Msg Receive',
                name='Msg received',
                response_time=(time.time() - init_time) * 1000,
                response_length=len(msg)
            )

    @task
    def login_task(self):
        self.init_time = time.time()

        self.ws = self.login()

        def _send_heartbeat():
            while True:
                self.ws.send("\n")
                time.sleep(20)

        def _receive():
            while True:
                res = self.ws.recv()
                self.handle_message(res)

        gevent.spawn(_send_heartbeat)
        gevent.spawn(_receive)
        time.sleep(3600)


class EchoLocust(HttpLocust):
    def __init__(self):
        super(EchoLocust, self).__init__()
        global USER_CREDENTIALS
        if USER_CREDENTIALS is None:
            """Read credentials from file"""
            with open('locust_users.csv', 'r') as f:
                reader = csv.reader(f, delimiter=";")
                USER_CREDENTIALS = list(reader)

    task_set = LDSTaskSet
    wait_time = between(0, 0.100)
