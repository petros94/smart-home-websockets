from __future__ import absolute_import
from __future__ import unicode_literals
from __future__ import print_function


from locust import HttpLocust, TaskSet, task, between
import time
import random
import csv

USER_CREDENTIALS = None

class LDSTaskSet(TaskSet):
    def __init__(self, parent):
        super().__init__(parent)
        self.usernames = []

    def on_start(self):
        pass

    def on_quit(self):
        pass

    @task
    def start_transaction(self):
        client = self.locust.client
        random_user = random.choice(USER_CREDENTIALS[-5000:])[0]
        print(random_user)
        response = client.post("/device", json={"destination": random_user, "command": "turn_on"})
        time.sleep(5)


class EchoLocust(HttpLocust):
    def __init__(self):
        super().__init__()
        global USER_CREDENTIALS
        with open('locust_users.csv') as f:
            reader = csv.reader(f, delimiter=";")
            global USER_CREDENTIALS
            USER_CREDENTIALS = list(reader)

    task_set = LDSTaskSet
    wait_time = between(0, 0.100)
