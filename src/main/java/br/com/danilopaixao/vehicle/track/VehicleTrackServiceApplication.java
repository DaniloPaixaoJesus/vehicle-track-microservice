package br.com.danilopaixao.vehicle.track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableRabbit
@EnableScheduling
public class VehicleTrackServiceApplication {
	
	@Value("${queue.vehicle.track.name}")
    private String vehicleTrackQueue;
	
	@Value("${queue.vehicle.service.update}")
	private String vehicleserviceUpdateQueue;
	
	@Value("${spring.redis.host}")
    private String REDIS_HOST;
	
	@Value("${spring.redis.port}")
    private int REDIS_PORT;
	
	@Value("${spring.redis.password}")
    private String REDIS_PASSWORD;
	
	@Value("${default.date.si.format.value:yyyy-MM-dd'T'HH:mm:ss}")
	private String defaultDateFormatSi;

    public static void main(String[] args) {
        SpringApplication.run(VehicleTrackServiceApplication.class, args);
    }
    
    @Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
    	RestTemplate restTemplate = new RestTemplate();
		final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverterSi = this
				.mappingJackson2HttpMessageConverterSi();
		List<HttpMessageConverter<?>> list = new ArrayList<>();
		list.add(mappingJackson2HttpMessageConverterSi);
		restTemplate.setMessageConverters(list);
		restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverterSi);
		return restTemplate;
	}
    
	private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverterSi() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new SimpleDateFormat(defaultDateFormatSi));
		jsonConverter.setObjectMapper(objectMapper);
		return jsonConverter;
	}
    
    @SuppressWarnings("deprecation")
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName(REDIS_HOST);
        jedisConFactory.setPort(REDIS_PORT);
        jedisConFactory.setPassword(REDIS_PASSWORD);
        return jedisConFactory;
    }
    
    JedisConnectionFactory jedisConnectionFactoryNoDeprecated() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(REDIS_PASSWORD));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
     
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
    
    @Bean("${queue.vehicle.track.name}")
    public Queue queueVehicleTrack() {
        return new Queue(vehicleTrackQueue, true);
    }
    
    @Bean("${queue.vehicle.service.update}")
    public Queue queueVehicleUpdate() {
        return new Queue(vehicleserviceUpdateQueue, true);
    }

}
