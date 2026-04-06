@e2e @login
Feature: User Authentication - Login
  As a registered user
  I want to log in to the application
  So that I can access protected resources

  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I enter username "tomsmith" and password "SuperSecretPassword!"
    And I click the login button
    Then I should be redirected to the secure area

  Scenario: Failed login with invalid credentials
    Given I am on the login page
    When I enter username "invalid" and password "wrongpassword"
    And I click the login button
    Then I should see an error message
