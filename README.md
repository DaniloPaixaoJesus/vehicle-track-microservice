### Solution
The Web app display all vehicles on the dashboard. Every vehicle should send a ping in order to keep its status ONLINE. After 1 minute without send any ping, the status will switch to OFFLINE automatically.
The solution is running into AWS EC2 instance http://swedish-challenge.danilopaixao.com.br.
You can send pings to verify if the vehicle is online on url http://swedish-challenge.danilopaixao.com.br:8085/send.html

### Architecture
![](https://https://s3.amazonaws.com/bucket.danilopaixao.com.br/spring-cloud-vehicle-solution.png)

The solution was built with Spring Cloud Microservices Chassis Framework.
Service discovery: Spring Cloud Netflix Eureka. 
Circuit breaker: Spring Cloud Netflix Hystrix.
API Gateway and Filter: Zuul API Gateway.
Springboot as a base of Spring Cloud framework and Business Microservices.

### Microservice
The services was built with spring core framework, springboot, springdata, springmvc and spring scheduled tasks.

### Rabbit MQ
A messaging broker to provide publish / subscribe, asynchronous processing and queues.
I guess that the quantity of vehicle’s ping request could increase a lot. Message broker could be a good deal to manage these mount of request.

### Redis Database
Fast key/value database. I used this database to check and maintain the status vehicle.
For instance, if the status is the same, I do not switch the status vehicle.

### MongoDB
Document database to store all information about vehicles and drivers. Each microservice has its own database instance.

### Web server NGINX
Container where web app is running. This web app can be deployed also into AWS S3 static site. The frontend was built with ReactJS. I also used Websocket to improve the user expirience.

### Docker/Docker compose
The whole solution is dockerized. Each microservice run into own container. I also use docker compose to create all containers.

### Deployment steps
Create the directory “swedq-challenge” as root of entire application.
Clone all repositories of the solution
https://github.com/DaniloPaixaoJesus/zuul-api-gateway-server.git<br>
https://github.com/DaniloPaixaoJesus/microservice-eureka-server.git<br>
https://github.com/DaniloPaixaoJesus/vehicle-microservice-eureka-client.git<br>
https://github.com/DaniloPaixaoJesus/driver-microservice-eureka-client.git<br>
https://github.com/DaniloPaixaoJesus/vehicle-track-microservice-eureka-client.git<br>
https://github.com/DaniloPaixaoJesus/vehicle-websocket-eureka-client.git<br>
https://github.com/DaniloPaixaoJesus/vehicle-reactjs-webapp.git<br>

Run the command “mvn clean install“ for whole repositories, except reactjs web app.<br>
Run the command “yarn build” to build reactjs web app.<br>
Copy files docker-compose.yml and rabbitmq.Dockerfile from “microservice-eureka-server” to “swedq-challenge”.<br>
Run the command “docker-compose up –build”<br>
Request GET http://[host]:8080/driver-service/api/v1/drivers/init in order to create all drivers.<br>
Request GET http://[host]:8080/vehicle-service/api/v1/vehicles/init in order to create all vehicles.<br>
Access [host]:8080/vehicle-websocket/vehicles.html to change the status of any vehicles in order to test switch status feature.<br>

Explain if it is possible to be in Serverless architecture and how?<br>
Yes, it is possible built some of these microservices in the serverless architecture. For instance, vehicle track microservice is a very short program with specif function. This microservice can be break into serverless functions.
