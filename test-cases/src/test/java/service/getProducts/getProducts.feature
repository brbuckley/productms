@Smoke
Feature: Get all Products from MS using filters

  Scenario: Request to find a Product that exists
    Given I want to get a Product
    And I ask the system for the product PRD0000001
    When I call the system
    Then The system gives me a list of products containing id: PRD0000001

  Scenario: Request to find all Products
    Given I want to get all Products
    When I call the system
    Then The system gives me a list of products

  Scenario: Request to find a Product by category
    Given I want to get all Products
    And I ask the system for all products of category: beer
    When I call the system
    Then The system gives me a list of products of category: beer

  Scenario: Request to find a Product by name
    Given I want to get all Products
    And I ask the system for all products with name like: neken
    When I call the system
    Then The system gives me a list of products containing name: Heineken

  Scenario: Request to find a page of 2 Products
    Given I want to get all Products
    And I ask the system for a page of all products with limit: 2
    When I call the system
    Then The system gives me a list of 2 products

  Scenario: Request to find all Products and Suppliers
    Given I want to get all Products
    And I ask the system for the suppliers
    When I call the system
    Then The system gives me a list of products and suppliers

  Scenario: Request to find all Products sorted by price
    Given I want to get all Products
    And I want the lowest price
    When I call the system
    Then The system gives me a list of products that has the price: 0.99 at first position

  Scenario: Request to find Products by ids
    Given I want to get all Products
    And I ask the system for the product PRD0000001,PRD0000002,PRD0000003
    When I call the system
    Then The system gives me a list of 3 products

  Scenario: Request to find a Product by name and category
    Given I want to get all Products
    And I ask the system for all products with name like: neken
    And I ask the system for all products of category: beer
    When I call the system
    Then The system gives me a list of products containing name: Heineken
    And The system gives me a list of products of category: beer

  Scenario: Request to find a Product that does not exist
    Given I want to get a Product
    And I ask the system for the product PRD1234567
    When I call the system
    Then The system shows: "Resource Not Found"

  Scenario: Request to find a Product with invalid id
    Given I want to get a Product
    And I ask the system for the product invalid
    When I call the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to find a Product with invalid name
    Given I want to get a Product
    And I ask the system for all products with name like: Lorem-ipsum-dolor-sit-amet--consectetur-adipiscing-elit
    When I call the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to find a Product with invalid category
    Given I want to get a Product
    And I ask the system for all products of category: invalid
    When I call the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to find a Product with invalid sort
    Given I want to get a Product
    And I want to sort by: asc.invalid
    When I call the system
    Then The system shows: "Missing required request parameters"