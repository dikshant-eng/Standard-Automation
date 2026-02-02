package com.SerenityBDD.pages.item;
import com.SerenityBDD.support.PageObjectOperations;
import org.openqa.selenium.By;
public class NewItem extends PageObjectOperations{

    public static final By PAGE_LOADED = By.cssSelector("body[data-ajax-state='complete']");
    public static final By ITEM_CODE = By.cssSelector("input[data-fieldname='item_code']");
    public static final By ITEM_GROUP = By.cssSelector("input[data-fieldname='item_group']");
//    public static final By DEFAULT_UNIT_OF_MEASURE = By.cssSelector("input[data-fieldname='stock_uom']");
//    public static final By OPENING_STOCK = By.cssSelector("input[data-fieldname='opening_stock']");
//    public static final By VALUATION_RATE = By.cssSelector("input[data-fieldname='valuation_rate']");
//    public static final By STANDARD_SELLING_RATE = By.cssSelector("input[data-fieldname='standard_rate']");
//    public static final By DISABLED = By.cssSelector("input[data-fieldname='disabled']");
//    public static final By ALLOW_ALTERNATIVE_ITEM = By.cssSelector("input[data-fieldname='allow_alternative_item']");
//    public static final By MAINTAIN_STOCK = By.cssSelector("input[data-fieldname='is_stock_item']");
//    public static final By HAS_VARIANTS = By.cssSelector("input[data-fieldname='has_variants']");
//    public static final By IS_FIXED_ASSET = By.cssSelector("input[data-fieldname='is_fixed_asset']");
      public static final By SAVE = By.cssSelector("button.primary-action[data-label='Save']");
}
