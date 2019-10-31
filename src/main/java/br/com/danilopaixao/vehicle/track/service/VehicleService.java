package br.com.danilopaixao.vehicle.track.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Vehicle;


@Service
public class VehicleService {
	
	@Value("${br.com.danilopaixao.service.vehicle.url}")
	private String vehicleRestApiUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod ="getVehicleFallBack",
			threadPoolKey = "getVehicleThreadPool",
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
	public Vehicle getVehicle(final String vin) {
		return restTemplate.getForObject(vehicleRestApiUrl+"/vehicles/"+vin, Vehicle.class);
	}
	public Vehicle getVehicleFallBack(final String id) {
		return new Vehicle("UNAVAILABLE", "", "", null, "");
	}
	
	
	@HystrixCommand(fallbackMethod ="updateVehicleFallBack",
			threadPoolKey = "updateVehicleThreadPool",
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
	public void updateVehicle(final String vin, StatusEnum status) {
		String url = vehicleRestApiUrl+"/vehicles/"+vin+"/status";
		restTemplate.put(url, status.toString());
	}
	public void updateVehicleFallBack(final String vin, StatusEnum status) {
		//TODO: armazenar dados no Redis
	}
	
}
