# Building the JETS Developer Package

The project uses the Gradle wrapper, so no local Gradle installation is
required. Use `./gradlew` on Linux/macOS or `.\gradlew.bat` on Windows.

## Prerequisites

- Java 17 JDK
- An HLA RTI (Portico 2.1.4) for running the application
- `lib/mms-rti-client-4.2.1.jar` present in `lib/` (included in this repository)

## Build and run from source

1. Open a terminal in the project root (`open-source/`).
2. Clean any previous build output:
   ```
   ./gradlew clean
   ```
3. Compile and assemble (skipping tests/checks):
   ```
   ./gradlew -x test -x check build
   ```
4. Launch the application (the DevPackage GUI should appear and the GraphQL
   server starts on port 8080):
   ```
   ./gradlew bootRun
   ```

## Create a distributable package

1. Build the project:
   ```
   ./gradlew build
   ```
2. Assemble the deployment bundle:
   ```
   ./gradlew packageTool
   ```

This produces `zip/devpackage_<version>.zip`, containing the application, its
runtime dependencies, the FOM modules, and the launch scripts (`run.bat` /
`run.sh` and their headless variants). To run the packaged build, unzip it,
set `RTI_HOME` to your Portico installation, and launch the appropriate script
(see the "Run a packaged build" section of the top-level [README](../README.md)).
