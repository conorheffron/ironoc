[![Java CI with Maven](https://github.com/conorheffron/ironoc/actions/workflows/maven.yml/badge.svg)](https://github.com/conorheffron/ironoc/actions/workflows/maven.yml)

![Proof HTML](https://github.com/conorheffron/ironoc/actions/workflows/proof-html.yml/badge.svg)

![Auto Assign](https://github.com/conorheffron/ironoc/actions/workflows/auto-assign.yml/badge.svg)

[![Deploy to Amazon ECS](https://github.com/conorheffron/ironoc/actions/workflows/aws.yml/badge.svg)](https://github.com/conorheffron/ironoc/actions/workflows/aws.yml)

### Docker Image
- [ironoc Docker Hub Link](https://hub.docker.com/repository/docker/conorheffron/ironoc/general)

## Hosted at:
- [ironoc.net](https://ironoc.net)
- [ironoc load balancer](https://ironoc-lb-6a36dafeeca59581.elb.eu-north-1.amazonaws.com/)

## About
Personal website / portfolio  [https://www.ironoc.net/](https://www.ironoc.net/)

## Tech Stack
Java 21 (LTS), Spring Boot 3.3, ReactJs 18, Maven 3.8, HTML5+CSS, Docker / Bash, AWS

## Run without cloning project:
```
docker run -d --restart=always -p 8080:8080 conorheffron/ironoc
```

## Run after project checkout (JDK 21 & Maven 3.8.3 required)
Build / Run App:
```
mvn clean package spring-boot:run
```

![image](screen-grabs/IDEA-Intellj-run.png)


### Build / Run (spin-up) Docker container:
```
docker image build -t ironoc .
docker compose up -d
docker logs ironoc-portfolio-1 -f
```

![image](screen-grabs/cli-docker.png)


### Tear-down:
```
docker-compose down
```

# Screenshot
![Home](screen-grabs/home-page.png)


