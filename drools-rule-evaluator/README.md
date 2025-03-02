# Drools Rule Evaluator

This project evaluates business rules using the Drools engine. It defines a set of rules to validate certain conditions related to orders and their items, such as checking the sum of item capacities and the relationship between item names and capacities.

## Rules

1. **Overall capacity**:  
   This rule checks if the sum of capacities of items in an order exceeds a threshold of 10. If the sum is greater than 10, a validation error is added.

    - **When**: The order contains items with a sum of capacities greater than 10.
    - **Then**: Adds a validation error: "BREACHED - capacitySum".

2. **Specific item capacity**:  
   This rule verifies if any item in the order has a capacity greater than 5 and the name is equal to 'C'. If both conditions are met, a validation error is added.

    - **When**: An item has a capacity greater than 5 and the name is 'C'.
    - **Then**: Adds a validation error: "BREACHED - nameAndCapacity".

## Usage

To evaluate the rules, you can make a POST request to the `/order` endpoint. This endpoint accepts an `Order` object, which contains a list of `Item` objects. The request will trigger the Drools rules, and if any validation errors occur, they will be added to the `ValidationDTO`.

### Example Request

```json
POST /order
Content-Type: application/json

{
  "order": {
    "items": [
      { "name": "A", "capacity": 3 },
      { "name": "C", "capacity": 6 }
    ]
  }
}
```

### Example Response

```json
{
  "validationErrors": [
    "BREACHED - nameAndCapacity"
  ]
}
```