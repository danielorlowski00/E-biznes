from selenium.webdriver.common.by import By
import time


def test_cart(driver):
    cart = driver.find_element(By.ID, "cart-nav")
    cart.click()
    time.sleep(1)
    cart_item = driver.find_element(By.ID, "cart-2")
    assert cart_item is not None
    cart_item = driver.find_element(By.ID, "cart-3")
    assert cart_item is not None
    button = driver.find_element(By.ID, "delete-cart-3")
    button.click()
    button = driver.find_element(By.ID, "order")
    button.click()
    time.sleep(1)


