from selenium.webdriver.common.by import By
import time

def test_login(driver):
    time.sleep(1)
    login = driver.find_element(By.ID, "login")
    password = driver.find_element(By.ID, "password")
    button = driver.find_element(By.ID, "log-in")
    assert login is not None
    assert password is not None
    assert button is not None
    login.send_keys("test")
    password.send_keys("test")
    button.click()
