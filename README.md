# Saikuru サイクル

A city bike application.

## To start it

First build it
```bash
cd server && ./gradlew bootBuildImage
```

Then create a `.env` file in the repository's root with your name
```text
CLIENT_NAME=FirstnameLastname
```

Then start it using Docker:
```bash
docker compose up
```

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