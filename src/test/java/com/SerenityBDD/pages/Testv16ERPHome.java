package com.SerenityBDD.pages;

import com.SerenityBDD.support.PageObjectOperations;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("https://standard.m.frappe.cloud/login")
public class Testv16ERPHome extends PageObjectOperations {
    public static final By PAGE_LOADED = By.id("page-Workspaces");
}
