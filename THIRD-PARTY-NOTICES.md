# Third-Party Notices

The JETS Developer Package is licensed under the Apache License, Version 2.0
(see [LICENSE](LICENSE)). It redistributes and/or depends on the third-party
components listed below, each of which remains under its own license.

This list is provided as a good-faith summary and is not a substitute for the
license texts shipped with each component. License names reflect the versions
listed here; verify against the artifact you actually build/ship. Runtime
dependency versions are resolved by Gradle from `build.gradle`; the versions
below reflect the resolved set at the time of writing.

---

## Bundled binaries (committed under `lib/`)

These JARs are redistributed directly in this repository.

| Component | Version | License | Notes |
|-----------|---------|---------|-------|
| Portico (HLA RTI) | see `lib/portico.jar` | CDDL 1.0 | Open-source HLA 1516-2010 RTI. https://github.com/openlvc/portico |
| Apache Pivot — pivot-core | 2.0.5 | Apache-2.0 | GUI toolkit. https://attic.apache.org/projects/pivot.html |
| Apache Pivot — pivot-wtk | 2.0.5 | Apache-2.0 | |
| Apache Pivot — pivot-wtk-terra | 2.0.5 | Apache-2.0 | |
| MMS RTI Client (`mms-rti-client`) | 4.2.1 | Redistributable (see note) | IVIR RTI abstraction layer + generated FOM code. Binary-only. |

> **MMS RTI Client** is provided by IVIR as a binary artifact. Source code is
> not distributed. It may be freely reused and included in other software.
> Contact IVIR for terms beyond redistribution.

---

## Runtime dependencies (resolved by Gradle)

### Apache License 2.0

- Spring Boot & Spring Framework (spring-boot, spring-core, spring-web,
  spring-webmvc, spring-context, spring-beans, spring-aop, spring-expression,
  spring-messaging, spring-websocket, spring-jcl) — 3.0.3 / 6.0.5
- Netflix DGS GraphQL (graphql-dgs-spring-boot-starter and related modules) — 7.6.0
- Apache Tomcat (embedded: core, el, websocket) — 10.1.5
- Jackson (core, databind, annotations, datatype-jdk8, datatype-jsr310,
  module-kotlin, module-parameter-names) — 2.14.2
- Kotlin standard library (stdlib, stdlib-common, stdlib-jdk7, stdlib-jdk8,
  reflect) — 1.7.22
- Kotlinx Coroutines (core-jvm, jdk8, reactive, reactor) — 1.6.4
- Apache Log4j (log4j-api, log4j-core, log4j-jul, log4j-slf4j2-impl) — 2.19.0
- Nitrite embedded database (org.dizitart:nitrite) — 4.3.0
- Datafaker — 1.9.0
- Generex — 1.0.2
- Project Reactor (reactor-core) — 3.5.3
- SnakeYAML — 1.33
- Jayway JsonPath (json-path) — 2.7.0
- json-smart / accessors-smart — 2.4.8
- Apache Commons Lang3 — 3.12.0
- Apache Commons Codec — 1.15
- Jasypt — 1.9.3
- Micrometer (commons, observation) — 1.10.4
- java-dataloader — 3.2.0
- JetBrains Annotations — 13.0
- Apollo Federation GraphQL Java support (federation-graphql-java-support,
  -api) — 3.0.0

### MIT License

- graphql-java — 20.6
- graphql-java-extended-scalars — 20.2
- SLF4J (slf4j-api) — 2.0.6

### BSD-style Licenses

- ASM (org.ow2.asm) — 9.1 — BSD 3-Clause
- Protocol Buffers (protobuf-java) — 3.22.1 — BSD 3-Clause
- dk.brics.automaton — 1.11-8 — BSD 2-Clause

### CDDL / EPL / Other

- Reactive Streams (reactive-streams) — 1.0.4 — MIT-0
- Jakarta Annotations API (jakarta.annotation-api) — 2.1.1 — EPL-2.0 / GPL-2.0-with-classpath-exception
- Jakarta Servlet API (jakarta.servlet-api) — 6.0.0 — EPL-2.0 / GPL-2.0-with-classpath-exception
- javax.annotation-api — 1.3.2 — CDDL-1.1 / GPL-2.0-with-classpath-exception

---

## Build tooling

- Gradle Wrapper (`gradle/wrapper/gradle-wrapper.jar`) — Apache-2.0

---

*If you add, remove, or upgrade a dependency, please update this file. A
generated report can be produced with a Gradle license plugin (e.g.
`com.github.jk1.dependency-license-report`) to keep this list authoritative.*
