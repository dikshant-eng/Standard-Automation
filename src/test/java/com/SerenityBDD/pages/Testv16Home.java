package com.SerenityBDD.pages;

import com.SerenityBDD.support.PageObjectOperations;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

@DefaultUrl("https://standard.m.frappe.cloud/desk")
public class Testv16Home extends PageObjectOperations {
    public static final By PAGE_LOADED = By.id("page-Workspaces");
}
