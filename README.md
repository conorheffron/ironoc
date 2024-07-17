[![Java CI with Maven](https://github.com/conorheffron/ironoc/actions/workflows/maven.yml/badge.svg)](https://github.com/conorheffron/ironoc/actions/workflows/maven.yml)

![Proof HTML](https://github.com/conorheffron/ironoc/actions/workflows/proof-html.yml/badge.svg)

![Auto Assign](https://github.com/conorheffron/ironoc/actions/workflows/auto-assign.yml/badge.svg)

# Docker Image
[ironoc Docker Hub Link](https://hub.docker.com/repository/docker/conorheffron/ironoc/general)

# Old Domain  (no longer supported)
~[ironoc website](http://www.ironoc.com)~

# About
Personal website / portfolio  http://www.ironoc.com/ (no longer hosted)

# Tech Stack
Java 21 (LTS), Spring Boot 3.3, HTML5+CSS, JQuery, Docker / Bash

# Run
Build / Run App:
```
mvn clean install test
mvn clean package spring-boot:run
```

Build / Run Docker container:
```
docker image build -t ironoc .
docker compose up -d
```

Spin-up Container: 
```
docker-compose up -d
```

Tear-down:
```
docker-compose down
```

# Screenshot
![Home](https://github.com/conorheffron/ironoc/assets/8218626/69cdb496-9773-4193-b465-92bc05a30800)

