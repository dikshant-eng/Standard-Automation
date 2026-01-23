package com.SerenityBDD.navigation;

import com.SerenityBDD.pages.StandardLogin;

public class NavigateTo {
    final StandardLogin standardloginPage = new StandardLogin();

    public void theLoginPage() {
        standardloginPage.open();
    }

}
