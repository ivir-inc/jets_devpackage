# JETS Developer Package

The **JETS Developer Package** ("DevPackage") is a Java 17 / Spring Boot
application that connects to an [HLA](https://en.wikipedia.org/wiki/High_Level_Architecture)
medical-simulation federation and exposes its data through a modern
**GraphQL API** (queries, mutations, and server-sent-event subscriptions).
It ships with an optional [Apache Pivot](https://attic.apache.org/projects/pivot.html)
desktop GUI and can also run headless.

It is intended as a starting point for developers who want to build tools,
dashboards, or integrations against a JETS medical-simulation federation
without writing HLA/RTI code directly — the DevPackage handles the RTI
plumbing and hands you GraphQL.

> Licensed under the [Apache License 2.0](LICENSE).

---

## Features

- Joins an HLA 1516-2010 (Evolved) federation as a federate and mirrors its
  objects/interactions (patients, physiology, vital signs, injuries,
  treatments, facilities, events, and more).
- Serves that data over **GraphQL** at `http://localhost:8080/graphql`.
- Streams live updates over **Server-Sent Events** at
  `http://localhost:8080/subscriptions`.
- Publishes updates back into the federation via GraphQL mutations.
- Optional desktop **GUI** for connecting/joining and inspecting state, or
  fully **headless** operation for automation.
- Local embedded storage via [Nitrite](https://github.com/nitrite/nitrite-java).
- Python client examples, including a vital-signs load generator.

---

## Architecture

```
  HLA Federation  <──RTI──>  MMS RTI Client  <──>  DevPackage (Spring Boot)
  (Portico / RTI)            (com.ivir.ral,        ├─ FED listeners  (HLA -> model)
                              devstudio.*)          ├─ Data fetchers  (GraphQL DGS)
                                                    ├─ Nitrite store
                                                    └─ Pivot GUI (optional)
                                                          │
                                              GraphQL / SSE over HTTP :8080
                                                          │
                                               Clients (Python, web, etc.)
```

---

## Prerequisites

- **Java 17 JDK**
- An **HLA RTI**. The DevPackage is built and tested against
  [Portico](https://github.com/openlvc/portico) **2.1.4**. A copy of
  `portico.jar` is bundled under `lib/`, but a full Portico installation is
  required at runtime (it provides the RTI and `RTI.rid`).
- The `lib/mms-rti-client-4.2.1.jar` artifact (IVIR's RTI abstraction layer +
  generated FOM code). This is redistributable and is included in `lib/`.

---

## Build

The project uses the Gradle wrapper — no local Gradle install needed.

```bash
./gradlew clean
./gradlew -x test -x check build
```

On Windows use `.\gradlew.bat` instead of `./gradlew`.

### Run from source (with GUI)

```bash
./gradlew bootRun
```

The DevPackage window should appear and the GraphQL server starts on
port `8080`.

### Package a distributable

```bash
./gradlew build
./gradlew packageTool
```

This produces `zip/devpackage_<version>.zip` containing the app, its
dependencies, the FOM modules, and launch scripts (`run.bat` / `run.sh` and
their headless variants).

### Run a packaged build

From the unzipped `devpackage/` directory, set `RTI_HOME` to your Portico
installation and launch:

```bash
# Linux/macOS
export RTI_HOME=/path/to/portico-2.1.4
./run.sh              # with GUI
./run_headless.sh     # headless

# Windows: edit RTI_HOME in run.bat, then
run.bat
```

---

## Configuration

Federation connection settings are read from
[`FederateConfig.txt`](src/main/resources/federate/FederateConfig.txt).
Uncomment and edit the relevant lines:

| Setting | Purpose |
|---------|---------|
| `crcHost` | RTI/CRC host address |
| `crcPort` | RTI/CRC port |
| `federateName` | This federate's name |
| `federationName` | Federation to join (default `JETS`) |
| `evolvedFomURL` | Base HLA MIM module |
| `additionalFomURLs` | Semicolon-separated FOM modules to load |

The FOM modules themselves (`Base.xml`, `Patient.xml`, `Communications.xml`,
`SimControl.xml`, etc.) live alongside the config file.

Spring/logging settings are in
[`application.properties`](src/main/resources/application.properties).

---

## Using the GraphQL API

- **Endpoint:** `POST http://localhost:8080/graphql`
- **Subscriptions (SSE):** `GET http://localhost:8080/subscriptions?query=<base64-encoded-query>`
- **GraphiQL:** enable by uncommenting the `spring.graphql.graphiql.*` lines
  in `application.properties`.

Example query (POST body):

```json
{ "query": "{ version { appVersion fomVersion } }" }
```

The GraphQL schema is defined by the `.graphqls` files under
[`src/main/resources/schema/`](src/main/resources/schema/).

---

## Python examples

Runnable client examples are under [`src/examples/python/`](src/examples/python/):

- `query_example.py` / `mutation_example.py` — basic query and mutation clients
- `sse.py` — subscribe to the SSE stream
- `vitalsigns_query.py` — a paced vital-signs load generator with CSV logging
  (see [README_vitalsigns_query.md](src/examples/python/README_vitalsigns_query.md))
- `analyze_recorder_multi_v14.py` — analysis of recorded output
  (see [README_analyze_recorder.md](src/examples/python/README_analyze_recorder.md))

Most examples require only Python 3.10+ and the `requests` package.

---

## Project layout

```
src/main/java/com/ivir/devpackage/
  fed/         HLA federate listeners (HLA -> internal model)
  model/       GraphQL DataFetchers and storage
  controller/  GraphQL query/response types and web client
  gui/         Apache Pivot desktop UI
  config/      Custom GraphQL scalar registrations
src/main/resources/
  schema/      GraphQL schema (.graphqls)
  federate/    FOM modules + FederateConfig.txt
src/examples/python/   Python client examples
docs/                  Build/deployment notes
```

---

## Contributing

Contributions are welcome. Please see [CONTRIBUTING.md](CONTRIBUTING.md) if
present, and open an issue or pull request. Contributions are accepted under
the terms of the Apache License 2.0.

---

## License

Copyright 2026 IVIR, Inc.

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) and
[NOTICE](NOTICE). Third-party components and their licenses are listed in
[THIRD-PARTY-NOTICES.md](THIRD-PARTY-NOTICES.md).
