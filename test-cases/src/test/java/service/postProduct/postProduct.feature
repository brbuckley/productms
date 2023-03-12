@Smoke
Feature: Create a new Product at MS

  Scenario: Request to create a new Product
    Given I want to create a new Product
    And The new Product should have name: Heineken, price: 10, category: beer, supplier: SUP0000001
    When The new product is received by the system
    Then The system gives me a product with a new id, and name: Heineken

  Scenario Outline: Request to create a Product with invalid information
    Given I want to create a new Product
    And The new Product should be:
      | name     | <name>     |
      | price    | <price>    |
      | category | <category> |
      | supplier | <supplier> |
    When The new product is received by the system
    Then The system shows: "Missing required request parameters"

    Examples:
      | name                                                    | price | category  | supplier   |
      | Heineken                                                | 0     | beer      | SUP0000001 |
      | Lorem-ipsum-dolor-sit-amet--consectetur-adipiscing-elit | 10    | wine      | SUP0000001 |
      | Heineken                                                | 10    | something | SUP0000001 |
      | Heineken                                                | 10    | beer      | SupplierId |

  Scenario: Request to create a Product missing information
    Given I want to create a new Product
    And The new Product should have name: Heineken, supplier: SUP0000001
    When The invalid product is received by the system
    Then The system shows: "Missing required request parameters"