### Entire Solution
The Web App display all vehicles on the dashboard. 
Every vehicle should send a ping in order to keep its status ONLINE. 
After 1 minute without send any ping, the status will switch to OFFLINE automatically.
The solution is running into AWS EC2 instance http://cloud-project.danilopaixao.com.br.
You can send pings to test vehicle online scenario on url http://cloud-project.danilopaixao.com.br:8085/send.html

### Architecture
![](https://s3.amazonaws.com/bucket.danilopaixao.com.br/spring-cloud-vehicle-solution.png)

The solution was built with Spring Cloud Microservices Chassis Framework.
Service discovery: Spring Cloud Netflix Eureka. 
Circuit breaker: Spring Cloud Netflix Hystrix.
API Gateway and Filter: Zuul API Gateway.
Spring Cloud Config
Springboot as a base of Spring Cloud framework and Business Microservices.
I also have coded unit test with junit, mockito and powermock.

### Microservice
This services was built with spring core framework, springboot, springdata, springmvc and spring scheduled tasks.

### Rabbit MQ
Messaging broker to provide publish / subscribe, asynchronous processing and queues.
Each "ping" request publish a message into MQ.
The communication between microservices is through MQ and Rest API.

### Redis Database
Fast key/value database. I have used this database to check and maintain the status vehicle/driver.

### MongoDB
Document database to store all information about vehicles, drivers and vehicles track.

### Web server NGINX
Container web where webapp is running. 
The frontend was built with ReactJS. I also used Websocket to improve the user expirience.

### Kubernet/Docker/Docker compose
The whole solution is dockerized. 
Each microservice run into own container. I also use docker compose to create all containers.

### Deployment steps
Create the directory as root of entire application.
Clone all repositories of the solution
https://github.com/DaniloPaixaoJesus/zuul-api-gateway-server.git<br>
https://github.com/DaniloPaixaoJesus/microservice-eureka-server.git<br>
https://github.com/DaniloPaixaoJesus/vehicle-microservice-eureka-client.git<br>
https://github.com/DaniloPaixaoJesus/driver-microservice-eureka-client.git<br>
https://github.com/DaniloPaixaoJesus/vehicle-track-microservice-eureka-client.git<br>
https://github.com/DaniloPaixaoJesus/vehicle-websocket-eureka-client.git<br>
https://github.com/DaniloPaixaoJesus/vehicle-reactjs-webapp.git<br>

Run the command “mvn clean install“ in all repositories, except reactjs web app.<br>
Run the command “yarn build” to build reactjs web app.<br>
Copy files docker-compose.yml and rabbitmq.Dockerfile from “microservice-eureka-server” to “swedq-challenge”.<br>
Run the command “docker-compose up –build”<br>
Request GET http://[host]:8080/driver-service/api/v1/drivers/init in order to create all drivers.<br>
Request GET http://[host]:8080/vehicle-service/api/v1/vehicles/init in order to create all vehicles.<br>
Access [host]:8080/vehicle-websocket/vehicles.html to change the status of any vehicles in order to test switch status feature.<br>