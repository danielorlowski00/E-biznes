import test_items
import test_users
import test_payments
import unittest


if __name__ == "__main__":
    suite = unittest.TestSuite()

    for TestModule in [test_items, test_users, test_payments]:
        suite.addTest(unittest.defaultTestLoader.loadTestsFromModule(TestModule))

    runner = unittest.TextTestRunner()
    runner.run(suite)
