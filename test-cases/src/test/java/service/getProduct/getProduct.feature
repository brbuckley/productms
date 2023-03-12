@Smoke
Feature: Get Product from MS

  Scenario: Request to find a Product that exists
    Given I want to get a Product
    And I ask the system for the product PRD0000001
    When I call the system
    Then The system gives me a product with id: PRD0000001

  Scenario: Request to find a Product with invalid id
    Given I want to get a Product
    And I ask the system for the product invalid
    When I call the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to find a Product that does not exist
    Given I want to get a Product
    And I ask the system for the product PRD1234567
    When I call the system
    Then The system shows: "Resource Not Found"