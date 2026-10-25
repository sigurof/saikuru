# Server

The Saikuru server application. 

## To get started

First, make sure to create `application-local.properties` and fill in your Client-Identifier

```bash
cp application-local.properties.template application-local.properties
sed -i.bak 's|Company/Name-ApplicationName|<your-client-identifier>|' application-local.properties && rm application-local.properties.bak
```

```bash
./gradlew bootRun
```

## To build and test
```bash
./gradlew build
```