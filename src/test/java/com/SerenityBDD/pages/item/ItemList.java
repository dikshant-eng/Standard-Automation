package com.SerenityBDD.pages.item;
import com.SerenityBDD.support.PageObjectOperations;
import org.openqa.selenium.By;
public class ItemList extends PageObjectOperations{

    public static final By PAGE_LOADED = By.cssSelector("body[data-ajax-state='complete']");
    public static final By ADD_ITEM = By.cssSelector("button.primary-action[data-label='Add Item']");
}
