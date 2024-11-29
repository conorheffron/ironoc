[![Java CI with Maven](https://github.com/conorheffron/ironoc/actions/workflows/maven.yml/badge.svg)](https://github.com/conorheffron/ironoc/actions/workflows/maven.yml)

![Proof HTML](https://github.com/conorheffron/ironoc/actions/workflows/proof-html.yml/badge.svg)

![Auto Assign](https://github.com/conorheffron/ironoc/actions/workflows/auto-assign.yml/badge.svg)

[![Deploy to Amazon ECS](https://github.com/conorheffron/ironoc/actions/workflows/aws.yml/badge.svg)](https://github.com/conorheffron/ironoc/actions/workflows/aws.yml)

### Docker Image
- [ironoc Docker Hub Link](https://hub.docker.com/repository/docker/conorheffron/ironoc/general)

## Hosted at:
- [ironoc.net](https://me.ironoc.net)
- [ironoc load balancer](https://ironoc-lb-6a36dafeeca59581.elb.eu-north-1.amazonaws.com/)

## About
Personal website / portfolio  [https://www.ironoc.net/](https://www.ironoc.net/)

## Tech Stack
Java 21 (LTS), Spring Boot 3.3, ReactJs 18, Maven 3.8, HTML5+CSS, 
    Docker / Bash, AWS, minikube, & kubectl.

## Run without cloning project:
```
docker run -d --restart=always -p 8080:8080 conorheffron/ironoc
```

## Run after project checkout (JDK 21 & Maven 3.8.3 required)
Build / Run App:
```
mvn clean package

mvn -DAWS_ACCESS_KEY_ID=<val1> \
    -DAWS_REGION=<val2> \
    -DAWS_SECRET_ACCESS_KEY=<val3> \
    -DAWS_SESSION_TOKEN=<val4> \
    spring-boot:run
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

# Local k8s cluster with Minikube:

MiniKube Install Notes for mac users
```
brew install kubectl
brew install virtualbox
brew install minikube
```

```
Oracle VirtualBox Manager v7.1.4

% kubectl version     
Client Version: v1.30.2
Kustomize Version: v5.0.4-0.20230601165947-6ce0bf390ce3
Server Version: v1.31.0

% minikube version
minikube version: v1.34.0
commit: 210b148df93a80eb872ecbeb7e35281b3c582c61

% docker version
Client:
Version:           27.3.1
API version:       1.47
Go version:        go1.22.7
Git commit:        ce12230
Built:             Fri Sep 20 11:38:18 2024
OS/Arch:           darwin/amd64
Context:           desktop-linux
```

- Open terminal
```
cd k8s/

# (clean-up & again after local testing complete)
minikube delete  
    
minikube start --driver=docker
kubectl cluster-info

minikube dashboard
```

### Then change namespace in browser from default to ironoc-ns
- i.e. http://127.0.0.1:56414/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/#/pod?namespace=ironoc-ns

### Open New tab in terminal & create deployment
```
docker image build -t ironoc .
minikube image load ironoc:latest

kubectl create ns ironoc-ns

kubectl apply -f k8s/ironoc.yaml --namespace=ironoc-ns

kubectl get pods --namespace=ironoc-ns
kubectl get deployment --namespace=ironoc-ns

kubectl expose deployment ironoc-app-deployment --type=NodePort --namespace=ironoc-ns

kubectl get svc --namespace=ironoc-ns

minikube service ironoc-app-deployment --url --namespace=ironoc-ns
```

### Open New tab in terminal & tail logs
```
% kubectl get pods --namespace=ironoc-ns
NAME                                     READY   STATUS    RESTARTS   AGE
ironoc-app-deployment-6d84f75b44-kpqpj   1/1     Running   0          3m37s

kubectl logs ironoc-app-deployment-6d84f75b44-kpqpj -f --namespace=ironoc-ns
```
