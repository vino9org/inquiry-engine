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
    CasaAccountsByCustomer(customerId: "111") {
            accountId
            currency
            transactions { 
                refId
                amount
                valueDate 
            }
    }
}
```

You should get the following response

```json
{
    "data": {
        "CasaAccountsByCustomer": [
            {
                "accountId": "123",
                "currency": "SGD",
                "transactions": [
                    {
                        "refId": "10000001",
                        "amount": "100.00",
                        "valueDate": "2023-05-01"
                    }
                ]
            },
            {
                "accountId": "124",
                "currency": "USD",
                "transactions": [
                    {
                        "refId": "10000002",
                        "amount": "12.00",
                        "valueDate": "2023-04-01"
                    }
                ]
            }
        ]
    }
}```
