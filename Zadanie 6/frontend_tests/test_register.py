from selenium.webdriver.common.by import By


def test_register(driver, url):
    driver.get(url)
    login = driver.find_element(By.ID, "login")
    password = driver.find_element(By.ID, "password")
    button = driver.find_element(By.ID, "register")
    assert login is not None
    assert password is not None
    assert button is not None
    login.send_keys("test")
    password.send_keys("test")
    button.click()
