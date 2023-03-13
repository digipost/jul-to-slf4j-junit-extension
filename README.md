[![Maven Central](https://maven-badges.herokuapp.com/maven-central/no.digipost/jul-to-slf4j-junit-extension/badge.svg)](https://maven-badges.herokuapp.com/maven-central/no.digipost/jul-to-slf4j-junit-extension)
[![Build and deploy](https://github.com/digipost/jul-to-slf4j-junit-extension/workflows/Build%20and%20deploy/badge.svg)](https://github.com/digipost/jul-to-slf4j-junit-extension/actions)
[![License](https://img.shields.io/badge/license-Apache%202-blue)](LICENSE)

# Digipost jul-to-slf4j bridge JUnit Extension

This is a JUnit Jupiter extension which will load the
[jul-to-slf4j bridge handler](https://www.slf4j.org/legacy.html#jul-to-slf4j)
for tests, to get logging done using the `java.util.logging` API routed to
SLF4J.

The preferred way to use this is to declare it as a test-dependency, and include
a `junit-platform.properties` file in the classpath for your tests, e.g. in
`src/test/resources/junit-platform.properties`:
```properties
junit.jupiter.extensions.autodetection.enabled=true
```

This will enable the extension to be automatically loaded by JUnit Jupiter, and
you do not need to specify the extension in every test.

Alternatively, if extension autodetection can not be enabled for your project
for any reason, the extension can be loaded using
```java
@ExtendWith(JavaLoggingToSlf4JExtension.class)
```

The extension detects if the jul-to-slf4j bridge handler is already installed
(e.g. by Spring Boot or other tooling which sets up the Slf4J stack), and will
not reinstall the bridge handler in such case.
