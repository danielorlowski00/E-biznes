import unittest

import requests

BASE_URL = 'http://127.0.0.1:8080'
ORDER_DOES_NOT_EXIST = "Order with given id does not exist"
USER_DOES_NOT_EXIST = "User with given id does not exist"
PAYMENT_DOES_NOT_EXIST = "Payment with given id does not exist"


# needs data from test_items.py
class PaymentsTest(unittest.TestCase):

    def setUp(self): pass

    # add order
    def test_step_1(self):
        # correct data
        orders = [{'id': 1, 'itemId': 2, 'quantity': 5, 'orderId': 1, 'userId': 1},
                  {'id': 2, 'itemId': 3, 'quantity': 5, 'orderId': 1, 'userId': 1}]
        response = requests.post(f"{BASE_URL}/addOrder", json=orders)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), orders)

        # correct data
        orders = [{'id': 3, 'itemId': 2, 'quantity': 2, 'orderId': 2, 'userId': 1},
                  {'id': 4, 'itemId': 3, 'quantity': 7, 'orderId': 2, 'userId': 1}]
        response = requests.post(f"{BASE_URL}/addOrder", json=orders)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), orders)

        # missing data
        orders = [{'id': 5, 'quantity': 3, 'orderId': 3, 'userId': 1},
                  {'id': 6, 'itemId': 3, 'quantity': 5, 'orderId': 3, 'userId': 1}]
        response = requests.post(f"{BASE_URL}/addOrder", json=orders)

        self.assertEqual(response.status_code, 400)

    # get order by id tests
    def test_step_2(self):
        # existing id
        orders = [{'id': 1, 'itemId': 2, 'quantity': 5, 'orderId': 1, 'userId': 1},
                  {'id': 2, 'itemId': 3, 'quantity': 5, 'orderId': 1, 'userId': 1}]
        response = requests.get(f"{BASE_URL}/getOrderById?id=1")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), orders)

        # not existing id
        response = requests.get(f"{BASE_URL}/getOrderById?id=3")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, ORDER_DOES_NOT_EXIST)

    # delete order tests
    def test_step_3(self):
        # existing id
        response = requests.delete(f"{BASE_URL}/deleteOrder?id=2")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, "2")

        # not existing id
        response = requests.delete(f"{BASE_URL}/deleteOrder?id=2")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, ORDER_DOES_NOT_EXIST)

    # pay tests
    def test_step_4(self):
        payment = [{'id': 1, 'orderId': 1, 'done': True, 'total': 169.89999999999998, 'userId': 1}]
        params = {'id': 1}
        response = requests.put(f"{BASE_URL}/pay", params=params)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), payment)

        params = {'id': 5}
        response = requests.put(f"{BASE_URL}/pay", params=params)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, PAYMENT_DOES_NOT_EXIST)

    # get payments tests
    def test_step_5(self):
        # get payments
        payments = [{"id": 1, "orderId": 1, "done": True, "total": 169.89999999999998, "userId": 1}]
        response = requests.get(f"{BASE_URL}/getPayments/1")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), payments)

        # get payments for not existing user
        response = requests.get(f"{BASE_URL}/getPayments/5")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, USER_DOES_NOT_EXIST)
