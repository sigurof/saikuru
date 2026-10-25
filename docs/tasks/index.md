# Task Management

Standin for JIRA/Linear/Trello. This document tracks and sequences work to be done on a loose ad-hoc basis.

## TODOs

[x] Add Wiremock test of new OsloCityBikeClient class
    the class returns the data on the same format as Oslo kommune
    Stubbed bysykkel API responses
    If Bysykkel API return 2xx, return data
    If Bysykkel API returns 4xx, return BysykkelApiClientException
    If Bysykkel API returns 5xx, return BysykkelApiInternalException
    If client-side bug (e.g. java programming issue), return OsloCityBikeClientException
[x] Add stubbed HTTP REST API with corresponding MicroserviceTest
[x] Add pre-commit hook for `cd server && ./gradlew test`
[ ] Add docker compose setup. Acceptance criteria: `docker compose up -d && curl http://localhost:8080/citybikes/stations && echo "Everything OK"`
[ ] Add BikeStationRepository, an internal abstraction that encapsulates the Oslo Bysykkel API. 
    Internally it may cache the responses for some time.
    Considering simple in-memory with Caffeine.
    There's no point in fetching the stations that often, since they're likely highly static
    Status could be fetched according to Oslo Kommune's own TTL (15 seconds).
[ ] Add app/MainApplication.java to represent the unadulterated java-only representation of our "microservice". Orchestrate calls to BikeStationRepository
[ ] Add Bike domain model, add BikeStation 