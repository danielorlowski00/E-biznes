import requests
import unittest

BASE_URL = 'http://127.0.0.1:8080'
ITEM_DOES_NOT_EXIST = "Item with given id does not exist"


class ItemsTest(unittest.TestCase):

    def setUp(self): pass

    # add item tests
    def test_step_1(self):
        # correct data
        item_data = {'id': 1, 'name': 'Test Item', 'categoryName': 'Test Category', 'price': 9.99}
        response = requests.post(f"{BASE_URL}/addItem", json=item_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), item_data)

        # correct data
        item_data = {'id': 2, 'name': 'Test Item 2', 'categoryName': 'Test Category 2', 'price': 15.99}
        response = requests.post(f"{BASE_URL}/addItem", json=item_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), item_data)

        # correct data
        item_data = {'id': 3, 'name': 'Test Item 3', 'categoryName': 'Test Category 3', 'price': 17.99}
        response = requests.post(f"{BASE_URL}/addItem", json=item_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), item_data)

        # missing data
        item_data = {'id': 1, 'categoryName': 'Test Category', 'price': "9.54"}
        response = requests.post(f"{BASE_URL}/addItem", json=item_data)

        self.assertEqual(response.status_code, 400)

    # get item by id tests
    def test_step_2(self):
        # existing id
        item_data = {'id': 1, 'name': 'Test Item', 'categoryName': 'Test Category', 'price': 9.99}
        response = requests.get(f"{BASE_URL}/getItemById?id=1", json=item_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), item_data)

        # not existing id
        response = requests.get(f"{BASE_URL}/getItemById?id=5", json=item_data)
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, ITEM_DOES_NOT_EXIST)

    # get all items tests
    def test_step_3(self):
        items = [{'id': 1, 'name': 'Test Item', 'categoryName': 'Test Category', 'price': 9.99},
                 {'id': 2, 'name': 'Test Item 2', 'categoryName': 'Test Category 2', 'price': 15.99},
                 {'id': 3, 'name': 'Test Item 3', 'categoryName': 'Test Category 3', 'price': 17.99}]
        response = requests.get(f"{BASE_URL}/getItems")

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), items)

    # delete item by id tests
    def test_step_4(self):
        # existing id
        response = requests.delete(f"{BASE_URL}/deleteItem?id=1")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, "1")

        # not existing id
        response = requests.delete(f"{BASE_URL}/deleteItem?id=1")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, ITEM_DOES_NOT_EXIST)

    # update item by id tests
    def test_step_5(self):
        item_data = {'id': 2, 'name': 'Updated Name', 'categoryName': 'Updated Category', 'price': 15.99}
        response = requests.put(f"{BASE_URL}/updateItem", json=item_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.json(), item_data)

        item_data = {'id': 5, 'name': 'Test', 'categoryName': 'Test', 'price': 15.99}
        response = requests.put(f"{BASE_URL}/updateItem", json=item_data)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.text, ITEM_DOES_NOT_EXIST)
