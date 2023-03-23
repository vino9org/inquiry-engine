# GraphQL API for account inquiry
Account inquiry engine that exposes GraphQL endpoint for querying data stored in MongoDB

Tech stack:
* [Sprint Boot](https://spring.io/)
* [Netflx Domain Graph Service framework](https://netflix.github.io/dgs/)
* [Embedded MongoDB]() is used to simplify setup and testing

The project is still in POC stage. Contributions welcome!

## Get Started
Checkout the [GraphQL Schema](src/main/resources/schema/schema.graphqls). Then use your favrite GraphQL client and send the following query

```text
{
    getCasaAccountDetail(accountId:"123") {
        accountId
    }
}
```

You should get the following response

```json
{
  "data": {
    "getCasaAccountDetail": [
      {
        "accountId": "123"
      }
    ]
  }
}

```