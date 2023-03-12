@Smoke
Feature: Update a new Product at MS

  Scenario: Request to update a Product
    Given I want to update a Product
    And The Product PRD0000001 should have name: Heineken, price: 10, category: beer
    When The product is received by the system
    Then The system gives me a product with id PRD0000001 and name: Heineken

  Scenario Outline: Request to update a Product with invalid info
    Given I want to update a Product
    And The Product should be:
      | productId | <productId> |
      | name      | <name>      |
      | price     | <price>     |
      | category  | <category>  |
    When The product is received by the system
    Then The system shows: "Missing required request parameters"

    Examples:
      | productId  | name                                                    | price | category  |
      | PRD0000001 | Heineken                                                | 0     | beer      |
      | PRD0000001 | Lorem-ipsum-dolor-sit-amet--consectetur-adipiscing-elit | 10    | wine      |
      | PRD0000001 | Heineken                                                | 10    | something |
      | something  | Heineken                                                | 10    | beer      |

  Scenario: Request to update a Product missing information
    Given I want to update a Product
    And The Product PRD0000001 should have name: Heineken
    When The incomplete product is received by the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to update a Product that does not exist
    Given I want to update a Product
    And The Product PRD1234567 should have name: Heineken, price: 10, category: beer
    When The product is received by the system
    Then The system shows: "Resource Not Found"