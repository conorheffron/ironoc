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
mvn clean package spring-boot:run
```

![image](https://github.com/user-attachments/assets/01f419e1-d8df-47a8-8f9b-bfd87d57d182)


Build / Run (spin-up) Docker container:
```
docker image build -t ironoc .
docker compose up -d
```
![image](https://github.com/user-attachments/assets/964897f5-917e-469b-99f4-71205227e371)


Tear-down:
```
docker-compose down
```

# Screenshot
![Home](https://github.com/user-attachments/assets/02290f96-d087-4a1b-98b3-0f3054d4dd25)


