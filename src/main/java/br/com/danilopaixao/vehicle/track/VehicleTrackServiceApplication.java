package br.com.danilopaixao.vehicle.track;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class VehicleTrackServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleTrackServiceApplication.class, args);
    }
    
    @Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
//		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//		clientHttpRequestFactory.setConnectTimeout(2000);
//		return new RestTemplate(clientHttpRequestFactory);
	}
}
