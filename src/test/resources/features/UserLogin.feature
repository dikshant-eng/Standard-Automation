Feature: Verify successful user login to ERP Next System

  Scenario: Verify Logging In through a User with Valid credentials
    Given I launch the browser and open the login page
    Then I should be on the "StandardLogin" page
    When I set the "Username" text field as "dikshant@korecent.com"
    And I set the "Password" text field as "Korecent@01"
    And I click the "Login" button
    Then I should be on the "Testv16Home" page
    Then I generate the selectors for the page
    And I stop the debugger here
