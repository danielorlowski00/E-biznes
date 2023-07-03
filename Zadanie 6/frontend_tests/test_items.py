from selenium.webdriver.common.by import By
import time


def test_items(driver):
    time.sleep(1)
    item = driver.find_element(By.ID, "item-2")
    assert item is not None
    item = driver.find_element(By.ID, "item-3")
    assert item is not None
    button = driver.find_element(By.ID, "item-2-add")
    button.click()
    button = driver.find_element(By.ID, "item-3-add")
    button.click()
