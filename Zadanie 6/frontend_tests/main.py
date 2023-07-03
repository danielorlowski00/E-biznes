from selenium import webdriver
from test_register import test_register
from test_login import test_login
from test_items import test_items
from test_cart import test_cart
from test_payments import test_payments

if __name__ == '__main__':
    driver = webdriver.Chrome()
    url = "http://localhost:3000/"
    test_register(driver, url)
    test_login(driver)
    test_items(driver)
    test_cart(driver)
    test_payments(driver, True)
    test_login(driver)
    test_payments(driver, False)
