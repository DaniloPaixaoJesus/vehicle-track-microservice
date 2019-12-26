package br.com.danilopaixao.vehicle.track.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Location;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackWSocket;


@Service
public class VehicleSocketService {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleSocketService.class);
	
	@Value("${br.com.danilopaixao.service.websocket.url}")
	private String urlVehicleSocketService;
	
	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod ="updateStatusWebSocketFallBack",
			threadPoolKey = "updateStatusWebSocketThreadPool",
			threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "75"),
					@HystrixProperty(name = "maxQueueSize", value = "35")
			},
			commandProperties = {
				@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
				@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
				@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
				@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
			}
	)
	public String updateStatusWebSocket(String vin, StatusEnum status, Location location) {
		logger.info("##VehicleSocketService#updateStatusWebSocket vin {}, status {}", vin, status);
		String url = urlVehicleSocketService +"/vehicle/" + vin + "/status";
		restTemplate.put(url, new VehicleTrackWSocket(vin, status.toString(), location));
		return vin;
	}
	public String updateStatusWebSocketFallBack(String vin, StatusEnum status, Location location) {
		logger.error("##VehicleSocketService#updateStatusWebSocket vin {}, status {}", vin, status);
		return vin;
	}
	
	
}
