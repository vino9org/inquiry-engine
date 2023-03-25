# GraphQL API for account inquiry

Account inquiry engine that exposes GraphQL endpoint for querying data stored in MongoDB

Tech stack:

* [Sprint Boot](https://spring.io/)
* [Netflx Domain Graph Service framework](https://netflix.github.io/dgs/)
* [Embedded MongoDB]() is used to simplify setup and testing

The project is still in POC stage. Contributions welcome!

## Get Started

Checkout the [GraphQL Schema](src/main/resources/schema/schema.graphqls). Run the project
by ```./mvnw clean spring-boot:run```, then use your favorite GraphQL client and send the following query
to ```http://localhost:8080/graphql```

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

You should get the following response

```json
{
    "data": {
        "CasaAccount": {
            "accountId": "123",
            "balance": 1000.0,
            "currency": "SGD",
            "transactions": {
                "edges": [
                    {
                        "node": {
                            "refId": "10000001"
                        }
                    }
                ],
                "pageInfo": {
                    "hasPreviousPage": false,
                    "hasNextPage": false
                }
            }
        }
    }
}
}```
