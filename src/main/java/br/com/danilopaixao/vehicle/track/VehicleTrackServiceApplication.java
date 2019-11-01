package br.com.danilopaixao.vehicle.track;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import br.com.danilopaixao.vehicle.track.repository.VehicleTrackMapRepository;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableRabbit
@EnableScheduling
public class VehicleTrackServiceApplication {
	
	@Value("${queue.vehicle.track.name}")
    private String vehicleTrackQueue;

    public static void main(String[] args) {
        SpringApplication.run(VehicleTrackServiceApplication.class, args);
    }
    
    @Bean
    public VehicleTrackMapRepository getVehicleTrackerMapRepository() {
    	return new VehicleTrackMapRepository();
    }
    
    @Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
    
    //redis://kN2CUQ0lVUmyPbNG9PbprtiOnvILRh9n@redis-19635.c52.us-east-1-4.ec2.cloud.redislabs.com:19635
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
//        jedisConFactory.setHostName("redis-19635.c52.us-east-1-4.ec2.cloud.redislabs.com");
//        jedisConFactory.setPort(19635);
//        jedisConFactory.setPassword("kN2CUQ0lVUmyPbNG9PbprtiOnvILRh9n");
        return jedisConFactory;
    }
     
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
    
    @Bean
    public Queue queue() {
        return new Queue(vehicleTrackQueue, true);
    }

}
