Feature: Verify successful user login to ERP Next System

  Scenario: Verify Logging In through a User with Valid credentials
    Given I launch the browser and open the login page
    Then I should be on the "StandardLogin" page
    When I set the "Username" text field as "dikshant@korecent.com"
    And I set the "Password" text field as "Korecent@01"
    And I click the "Login" button
    Then I should be on the "Testv16Home" page
    When I select "Item" option from the Search field in the header
    Then I should be on the "ItemList" page under the "item" workflow
    When I click the "Add Item" button
    Then I should be on the "New Item" dialog under the "Item" workflow
    When I click the "EDIT FULL FORM" button
    Then I should be on the "New Item" page under the "item" workflow
    When I set these fields with following values:
      | field       | fieldType      | value                           |
      | Item Code   | text field     | Dry Case Materials              |
      | Item Group  | autofill field | Products                        |
    And I click the "Save" button
#   Item has been created successfully ---->

    And I stop the debugger here
