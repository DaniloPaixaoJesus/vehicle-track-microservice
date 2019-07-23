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

import br.com.danilopaixao.vehicle.track.repository.VehicleTrackerMapRepository;

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
    public VehicleTrackerMapRepository getVehicleTrackerMapRepository() {
    	return new VehicleTrackerMapRepository();
    }
    
    @Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
//		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//		clientHttpRequestFactory.setConnectTimeout(2000);
//		return new RestTemplate(clientHttpRequestFactory);
	}
    
    /**
       	redis:
    		url: redis://${REDIS_HOST:localhost}:${REDIS_PORT:6379}
    	
    	  redis:
		    host: ${REDIS_HOST:localhost}
		    port: ${REDIS_PORT:6379}
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }
    
//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
//        jedisConFactory.setHostName("redis://redis:6379");
//        jedisConFactory.setPort(6379);
//        return jedisConFactory;
//    }
     
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
