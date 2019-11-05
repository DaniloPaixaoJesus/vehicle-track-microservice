### Micro service
This micro service was built with springboot, spring core framework, spring data, spring mvc and spring scheduled tasks.
JUnit, Mockito and PowerMock to provide automated unit test.

The solution was built with Spring Cloud Microservices Chassis Framework.
Service discovery: Spring Cloud Netflix Eureka. 
Circuit breaker: Spring Cloud Netflix Hystrix.
API Gateway and Filter: Zuul API Gateway.
Spring Cloud Config
Springboot as a base of Spring Cloud framework and Business Microservices.

### Rabbit MQ
Messaging broker to provide publish / subscribe, asynchronous processing and queues.
Each "ping" request publish a message into MQ.
The communication between microservices is through MQ and Rest API.

### Redis Database
Fast key/value database. I have used this database to check and maintain the status vehicle.

### Run steps
Install Java SDK 8+
Install Maven
Run the command “mvn spring-boot:run“ on console