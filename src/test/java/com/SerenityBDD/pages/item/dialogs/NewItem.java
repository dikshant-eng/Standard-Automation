package com.SerenityBDD.pages.item.dialogs;

import com.SerenityBDD.support.PageObjectOperations;
import org.openqa.selenium.By;

public class NewItem extends PageObjectOperations{
        public static final By DIALOG_LOADED = By.xpath("//div[contains(@class,'modal-dialog')]//h4[normalize-space()='New Item']");
        public static final By EDIT_FULL_FORM = By.xpath("//div[contains(@class,'modal-footer')]//button[normalize-space()='Edit Full Form']");
}
