package com.github.cstroe.spendhawk.it

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver

class AddAccountIT {
    fun getDriver(): WebDriver {
        return FirefoxDriver()
    }
}