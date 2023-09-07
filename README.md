# Alert system

## Motivation
The need to efficiently manage thresholds and alerts has become more critical as the number of projects and services has grown.
The goal of this project is to provide a centralized service that will manage thresholds and alerts for all projects and services.

A New Paradigm: Streamlined Alert Management
At its core, this project introduces a novel service that redefines how alerts and thresholds are managed.
Instead of defining some alerts in your project, we're introducing a unified service that takes charge of the alerts and thresholds logic.
This centralized approach not only saves valuable time but also enhances the consistency and accuracy of alert management.

## Key Goals and Benefits
- Efficiency: By centralizing alerts and thresholds logic, this project streamlines the management process, reducing redundancy and saving developers' valuable time.
- Consistency: With a unified service in place, the consistency of alerts and thresholds across projects is greatly improved, leading to a more coherent user experience.

## Prerequisites
- Java 8
- Maven 4.0
- Docker


## Configuration
- Docker Initialization
- Hibernate Configuration

== Building
To launch your tests:

__unit__
```
./mvnw clean test
```

__acceptance__
```
./mvnw clean test -Pacceptance-tests
```

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean compile exec:java
```




