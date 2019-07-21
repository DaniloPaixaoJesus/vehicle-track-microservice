package br.com.danilopaixao.vehicle.track.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import br.com.danilopaixao.vehicle.track.model.Vehicle;


@Service
public class VehicleService {
	
	@Value("${br.com.danilopaixao.service.vehicle.host}")
	private String hostVehicleService;
	
	@Value("${br.com.danilopaixao.service.vehicle.protocol}")
	private String protocolVehicleService;
	
	@Value("${br.com.danilopaixao.service.vehicle.version}")
	private String versionVehicleService;
	
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
		return restTemplate.getForObject(this.protocolVehicleService+"://"+this.hostVehicleService+"/api/"+this.versionVehicleService+"/vehicles/"+vin, Vehicle.class);
	}
	public Vehicle getVehicleFallBack(final String id) {
		return new Vehicle("UNAVAILABLE", "", "", "", "");
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
	public void updateVehicle(final String vin, String status) {
		String url = this.protocolVehicleService+"://"+this.hostVehicleService+"/api/"+this.versionVehicleService+"/vehicles/"+vin+"/status";
		restTemplate.put(url, status);
	}
	public void updateVehicleFallBack(final String vin, String status) {
		//TODO: armazenar dados no Redis
	}
	
	

}
