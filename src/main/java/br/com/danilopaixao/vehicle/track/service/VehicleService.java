package br.com.danilopaixao.vehicle.track.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Location;
import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.model.VehicleList;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;


@Service
public class VehicleService {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
	
	@Value("${br.com.danilopaixao.service.vehicle.url}")
	private String vehicleRestApiUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	VehicleTrackQueueSender vehicleTrackQueueSender; 
	
	@HystrixCommand(fallbackMethod ="getAllOnLineVehicleFallBack",
			threadPoolKey = "getAllOnLineVehicleThreadPool",
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
	public VehicleList getAllOnLineVehicle() {
		return restTemplate.getForObject(vehicleRestApiUrl+"/vehicles", VehicleList.class);
	}
	public VehicleList getAllOnLineVehicleFallBack() {
		logger.error("error vehicle rest service ##VehicleService#getAllVehicle({})");
		return null;
	}
	
	@HystrixCommand(fallbackMethod ="getAllVehicleFallBack",
			threadPoolKey = "getAllVehicleThreadPool",
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
	public VehicleList getAllVehicle() {
		return restTemplate.getForObject(vehicleRestApiUrl+"/vehicles", VehicleList.class);
	}
	//TODO: REMOVER FALLBACK
	public VehicleList getAllVehicleFallBack() {
		logger.error("error vehicle rest service ##VehicleService#getAllVehicle({})");
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		
		vehicleList.add(new Vehicle("123", "9898989", "Danilo", 
				StatusEnum.ON, "134124123", 
				new VehicleTrack("123", "queue", StatusEnum.ON, LocalDateTime.now(), 
								new Location())));
		
		vehicleList.add(new Vehicle("123456", "9898989", "Danilo", 
						StatusEnum.ON, "134124123", 
						new VehicleTrack("123456", "queue", StatusEnum.ON, LocalDateTime.now(), 
										new Location())));
		VehicleList vehicleListReturn = new VehicleList(vehicleList); 
		return vehicleListReturn;
	}
	
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
		if(StringUtils.isEmpty(vin)) {
			return null;
		}
		return restTemplate.getForObject(vehicleRestApiUrl+"/vehicles/"+vin, Vehicle.class);
	}
	public Vehicle getVehicleFallBack(final String vin) {
		logger.error("error vehicle rest service ##VehicleService#getVehicle({})", vin);
		return new Vehicle(vin, "NOTFOUND", "NOTFOUND", StatusEnum.OFF, "NOTFOUND");
	}
	
	
	@HystrixCommand(fallbackMethod ="updateVehicleStatusFallBack",
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
	public void updateVehicleStatus(final String vin, StatusEnum status, Location location){
		if(StringUtils.isEmpty(vin)
				|| StringUtils.isEmpty(status)
				|| StringUtils.isEmpty(location)) {
			logger.error("invalid argument ##VehicleService#updateVehicle({}, {}, {})", vin, status, location);
			throw new IllegalArgumentException("location, vin or status null");
		}
		logger.info("##VehicleService#updateVehicle({}, {}, {})", vin, status, location);
		try {
			vehicleTrackQueueSender.sendToVehicleServiceQueue(vin, status, location);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	public void updateVehicleStatusFallBack(final String vin, StatusEnum status, Location location) {
		logger.error("error vehicle rest service ##VehicleService#updateVehicle({}, {}, {})", vin, status, location);
	}
	
}
