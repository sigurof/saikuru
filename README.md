# Saikuru サイクル

A city bike application.

## Run with Docker

All you need is Docker. Create a `.env` file in the repository's root with your name. It is used to build the
`Client-Identifier` header sent to Oslo City Bike.
```text
CLIENT_NAME=FirstnameLastname
```

Then build and start it:
```bash
docker compose up
```


## Run through Gradle

You can also run the server directly with `./gradlew bootRun` (requires JDK 21). See the
[server README](./server/README.md) for how to set that up.

## Test it out

The API is then available on http://localhost:8080, e.g.
```bash
curl "http://localhost:8080/citybike/stations?lat=59.9105&lon=10.7505&max=5"
```

After changing the code, rebuild the image with `docker compose up -d --build`.


## Pre-commit

This repo uses [pre-commit](https://pre-commit.com) as a simplified "CI pipeline". Install it with e.g.
`brew install pre-commit` or `pipx install pre-commit` and enable it:

```bash
pre-commit install
```

## Task management

You can find tasks and TODO's organized in [plan.md](./docs/tasks/index.md)

## What it is

This description is *aspirational* (not fully implemented yet):

Saikuru is intended to be a Client/Server application that enables users to find bikes and e-bikes close to their
location.

You can find further brainstorming and details here [is described here](./docs/product/index.md).