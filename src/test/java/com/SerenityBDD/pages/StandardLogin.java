package com.SerenityBDD.pages;

import com.SerenityBDD.support.PageObjectOperations;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("https://standard.m.frappe.cloud/login")
public class StandardLogin extends PageObjectOperations {
    public static final By PAGE_LOADED = By.id("page-login");
    public static final By USERNAME = By.id("login_email");
    public static final By PASSWORD = By.id("login_password");
    public static final By LOGIN = By.cssSelector("section.for-login button.btn-login");
}
