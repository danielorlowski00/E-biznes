from selenium.webdriver.common.by import By
import time


def test_payments(driver, pay):
    payments = driver.find_element(By.ID, "payments-nav")
    payments.click()
    time.sleep(1)
    payment = driver.find_element(By.ID, "payment-3")
    assert payment is not None
    if pay:
        button = driver.find_element(By.ID, "pay-3")
        assert button is not None
        button.click()
        time.sleep(1)
        main_window_handle = driver.current_window_handle
        alert = driver.switch_to.alert
        assert alert.text == "Zamówienie opłacone"
        alert.accept()
        driver.switch_to.window(main_window_handle)
    button = driver.find_element(By.ID, "signout-nav")
    assert button is not None
    button.click()

