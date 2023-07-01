import requests
import unittest

BASE_URL = 'http://127.0.0.1:8080'
USER_DOES_NOT_EXIST = "User with given id does not exist"


# needs data from test_items.py
class UsersTest(unittest.TestCase):

    def setUp(self): pass

    # create new users tests
    def test_step_1(self):
        # correct data
        user_data = {'id': 1, 'login': 'login', 'password': 'password'}
        response = requests.post(f"{BASE_URL}/register", json=user_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, "true")

        # correct data
        user_data = {'id': 2, 'login': 'login2', 'password': 'password2'}
        response = requests.post(f"{BASE_URL}/register", json=user_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, "true")

        # already existing user
        user_data = {'id': 2, 'login': 'login', 'password': 'password'}
        response = requests.post(f"{BASE_URL}/register", json=user_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, "false")

    # get all users tests
    def test_step_2(self):
        users = [{'id': 1, 'login': 'login', 'password': 'password'},
                 {'id': 2, 'login': 'login2', 'password': 'password2'}]
        response = requests.get(f"{BASE_URL}/getUsers")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), users)

    # delete user tests
    def test_step_3(self):
        # existing id
        response = requests.delete(f"{BASE_URL}/deleteUser?id=2")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, "2")

        # not existing id
        response = requests.delete(f"{BASE_URL}/deleteUser?id=2")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, USER_DOES_NOT_EXIST)

    # get user by id
    def test_step_4(self):
        # existing id
        user_data = {'id': 1, 'login': 'login', 'password': 'password'}
        response = requests.get(f"{BASE_URL}/getUserById?id=1")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), user_data)

        # not existing id
        response = requests.get(f"{BASE_URL}/getUserById?id=5")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, USER_DOES_NOT_EXIST)

    # update item by id tests
    def test_step_5(self):
        user_data = {'id': 1, 'login': 'LOGIN', 'password': 'PASSWORD'}
        response = requests.put(f"{BASE_URL}/updateUser", json=user_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), user_data)

        user_data = {'id': 5, 'login': 'login', 'password': 'password'}
        response = requests.put(f"{BASE_URL}/updateUser", json=user_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, USER_DOES_NOT_EXIST)

    # log in tests
    def test_step_6(self):
        # correct password and login
        user_data = {'id': 1, 'login': 'LOGIN', 'password': 'PASSWORD'}
        response = requests.post(f"{BASE_URL}/login", json=user_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, "1")

        # wrong credentials / not existing user
        user_data = {'id': 1, 'login': 'login2', 'password': 'password2'}
        response = requests.post(f"{BASE_URL}/login", json=user_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, "-1")
