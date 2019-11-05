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
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableRabbit
@EnableScheduling
public class VehicleTrackServiceApplication {
	
	@Value("${queue.vehicle.track.name}")
    private String vehicleTrackQueue;
	
	@Value("${spring.redis.host}")
    private String HOST;
	
	@Value("${spring.redis.port}")
    private int PORT;
	
	@Value("${spring.redis.password}")
    private String PASSWORD;

    public static void main(String[] args) {
        SpringApplication.run(VehicleTrackServiceApplication.class, args);
    }
    
    @Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
    
    @SuppressWarnings("deprecation")
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName(HOST);
        jedisConFactory.setPort(PORT);
        jedisConFactory.setPassword(PASSWORD);
        return jedisConFactory;
    }
    
    JedisConnectionFactory jedisConnectionFactoryNoDeprecated() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(HOST, PORT);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(PASSWORD));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
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
