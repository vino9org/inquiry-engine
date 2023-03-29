# GraphQL API for account inquiry

Account inquiry engine that exposes GraphQL endpoint for querying data stored in MongoDB

Tech stack:

* [Sprint Boot](https://spring.io/)
* [Netflx Domain Graph Service framework](https://netflix.github.io/dgs/)
* [Embedded MongoDB]() is used to simplify setup and testing

The project is still in POC stage. Contributions welcome!

## Get Started

Checkout the [GraphQL Schema](src/main/resources/schema/schema.graphqls). Run the project
by ```./mvnw clean spring-boot:run```, then use browser to navigate to ```http://localhost:8080/graphiql``` to use the GraphQL navigator.

Query example:
```text
{
    CasaAccount(accountId: "123") {
            accountId
            balance
            currency
            transactions {
                edges {
                    node {
                        refId
                    }
                }
                pageInfo {
                    hasPreviousPage
                    hasNextPage
                }
            }
    }
}
```

Result

```json
{
  "data": {
    "CasaAccount": {
      "accountId": "123",
      "balance": 1000,
      "currency": "SGD",
      "transactions": {
        "edges": [
          {
            "node": {
              "refId": "10000001",
              "currency": "SGD",
              "amount": 101
            }
          },
          {
            "node": {
              "refId": "10000002",
              "currency": "SGD",
              "amount": 102
            }
          },
          {
            "node": {
              "refId": "10000003",
              "currency": "SGD",
              "amount": 103
            }
          },
          {
            "node": {
              "refId": "10000004",
              "currency": "SGD",
              "amount": 104
            }
          },
          {
            "node": {
              "refId": "10000005",
              "currency": "SGD",
              "amount": 105
            }
          },
          {
            "node": {
              "refId": "10000006",
              "currency": "SGD",
              "amount": 106
            }
          },
          {
            "node": {
              "refId": "10000007",
              "currency": "SGD",
              "amount": 107
            }
          },
          {
            "node": {
              "refId": "10000008",
              "currency": "SGD",
              "amount": 108
            }
          },
          {
            "node": {
              "refId": "10000009",
              "currency": "SGD",
              "amount": 109
            }
          },
          {
            "node": {
              "refId": "10000010",
              "currency": "SGD",
              "amount": 110
            }
          }
        ],
        "pageInfo": {
          "hasPreviousPage": false,
          "hasNextPage": true,
          "endCursor": "1"
        }
      }
    }
  }
}
```
