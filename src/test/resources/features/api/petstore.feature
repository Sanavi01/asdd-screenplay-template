@api @petstore
Feature: Pet Store API - Pet Management
  As an API consumer
  I want to manage pets in the Pet Store
  So that I can perform CRUD operations

  Background:
    Given the API client is configured

  Scenario: Create a new pet
    When I create a pet with name "Buddy" and status "available"
    Then the response status code should be 200

  Scenario: Get an existing pet
    Given I have created a pet with name "Rex" and status "available"
    When I retrieve the pet by id
    Then the response status code should be 200

  Scenario: Update an existing pet
    Given I have created a pet with name "Max" and status "available"
    When I update the pet status to "sold"
    Then the response status code should be 200

  Scenario: Delete an existing pet
    Given I have created a pet with name "Luna" and status "available"
    When I delete the pet by id
    Then the response status code should be 200

  Scenario Outline: Create pet with different statuses
    When I create a pet with name "<name>" and status "<status>"
    Then the response status code should be 200
    Examples:
      | name    | status    |
      | Fido    | available |
      | Shadow  | pending   |
      | Goldie  | sold      |
