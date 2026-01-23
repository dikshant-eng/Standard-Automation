package com.SerenityBDD.pages;

import com.SerenityBDD.support.PageObjectOperations;
import org.openqa.selenium.By;

public class SelectApp1 extends PageObjectOperations {
    public static final By PAGE_LOADED = By.className("apps-container");
    public static final By ERP_NEXT = By.cssSelector("a[href='/app/home']");
    public static final By CRM = By.cssSelector("a[href='/crm']");
}
