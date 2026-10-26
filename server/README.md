# Server

The Saikuru server application.

## Run it with Gradle

Requires JDK 21.

First, create `application-local.properties` and fill in your name. It is used to build the `Client-Identifier`
header sent to Oslo City Bike.

```bash
cp application-local.properties.template application-local.properties
sed -i.bak 's|FirstnameLastname|<YourName>|' application-local.properties && rm application-local.properties.bak
```

Then start the server:

```bash
./gradlew bootRun
```

The API is then available on http://localhost:8080, e.g.

```bash
curl "http://localhost:8080/citybike/stations?lat=59.9105&lon=10.7505&max=5"
```

Instead of `application-local.properties` you can also pass your name as an environment variable:

```bash
CLIENT_NAME=<YourName> ./gradlew bootRun
```

## To build and test
```bash
./gradlew build
```
