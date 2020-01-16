### Microservice
This micro service was built with springcloud, springboot, spring core framework, spring data, spring mvc and spring scheduled tasks.
JUnit, Mockito and PowerMock to provide automated unit test.
This microservice track geolocations. The current geolocation is recorded into redisdb, the whole geolocation track is recorded in the document into mongodb.

The solution was built with Spring Cloud Microservices Chassis Framework.
Service discovery: Spring Cloud Netflix Eureka. 
Circuit breaker: Spring Cloud Netflix Hystrix.
API Gateway and Filter: Zuul API Gateway.
Spring Cloud Config
Springboot as a base of Spring Cloud framework and Business Microservices.

### Rabbit MQ
Messaging broker to provide publish / subscribe, asynchronous processing and queues.
Each "ping" request publish a message into MQ. The communication between microservices is through MQ and/or Rest API calls.

### Redis Database
Fast key/value database. I have used this database to check and maintain the vehicle status and current geolocation.
