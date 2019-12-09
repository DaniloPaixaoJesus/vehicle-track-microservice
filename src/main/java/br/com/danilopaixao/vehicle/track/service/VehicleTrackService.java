package br.com.danilopaixao.vehicle.track.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.mapper.VehicleTrackCacheMapper;
import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.model.VehicleList;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackMongoRepository;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackMongoRepository2;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackRedisRepository;

@Service
public class VehicleTrackService {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleTrackService.class);	
	
	@Value("${br.com.danilopaixao.vehicle.track.secondstooffline}")
	private int secondsToOfffline;
	
	@Autowired
	private VehicleTrackRedisRepository vehicleTrackRedisRepository;
	
	@Autowired
	private VehicleTrackMongoRepository vehicleTrackMongoRepository;
	
	@Autowired
	private VehicleTrackMongoRepository2 vehicleTrackMongoRepository2;
	
	@Autowired
	private VehicleTrackQueueSender vehicleTrackQueueSender;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Value("${queue.vehicle.track.name}")
	private String queueVehicleTrackName;
	
	@Scheduled(cron = "0/59 * * * * ?")
	public void processOffLineVehicle() {
		Iterable<VehicleTrackCache> listVehicleTrackCache = this.findAllVehicleTrackCache();
		if(listVehicleTrackCache == null) {
			VehicleList vehicleList = vehicleService.getAllVehicle();
			if( vehicleList == null
					|| vehicleList.getVehicleList() == null
					|| vehicleList.getVehicleList().isEmpty()) {
				logger.info("##Execution offline vehicle routine: NO VEHICLE");
				return;	
			}
			listVehicleTrackCache = toVehicleTrackCache(vehicleList.getVehicleList());
		}
		listVehicleTrackCache.forEach(vehicleTrack -> {
				if(vehicleTrack.getStatus() == StatusEnum.ON) {
					if(vehicleTrack.isOffLineVehicle(secondsToOfffline)) {
						logger.info("vin status OFF:"+vehicleTrack.getVin());
						vehicleTrack.setStatus(StatusEnum.OFF);
						vehicleTrack.setDtStatus(LocalDateTime.now());
						this.saveVehicleTrackCache(vehicleTrack);
						this.insertVehicleTrack(new VehicleTrack(vehicleTrack.getVin(), 
																	queueVehicleTrackName, 
																	StatusEnum.OFF, 
																	LocalDateTime.now(), 
																	vehicleTrack.getGeolocation()));
						vehicleService.updateVehicleStatus(vehicleTrack.getVin(), StatusEnum.OFF, vehicleTrack.getGeolocation());
					}
				}
			});
	}

	public List<VehicleTrack> findNearest(double latitude, double longitude, double distance) {
		return vehicleTrackMongoRepository2.findNearest(latitude, longitude, distance);
	}
	
	public VehicleTrackCache insertIntoQueueOnlineStatus(String vin, double[] position) throws Throwable {
		logger.info("##VehicleTrackService##insertIntoQueue: {} : {}", position);
		return vehicleTrackQueueSender.sendToQueueOnlineStatus(vin, position);
	}
	
	public VehicleTrackCache insertVehicleTrackCache(VehicleTrackCache vehicleTrackCache) {
		logger.info("##VehicleTrackService##insertVehicleTrackCache: {}", vehicleTrackCache.getVin());
		return vehicleTrackRedisRepository.save(vehicleTrackCache);
	}
	
	public VehicleTrack insertVehicleTrack(VehicleTrack vehicleTrack) {
		logger.info("##VehicleTrackService##insertVehicleTrack: {}", vehicleTrack.getVin());
		return vehicleTrackMongoRepository.save(vehicleTrack);
	}
	
	public VehicleTrackCache getVehicleTrackCache(String vin) {
		logger.info("##VehicleTrackService##getVehicleTrack: {}", vin);
		//TODO: use Optional class
		return vehicleTrackRedisRepository.findById(vin).orElse(null);
	}
	
	public Iterable<VehicleTrackCache> findAllVehicleTrackCache() {
		return vehicleTrackRedisRepository.findAll();
	}

	public VehicleTrackCache saveVehicleTrackCache(VehicleTrackCache vehicleTrackCache) {
		logger.info("##VehicleTrackService##save: {}/{}", vehicleTrackCache.getVin(), vehicleTrackCache.getStatus());
		return vehicleTrackRedisRepository.save(vehicleTrackCache);
	}
	
	public VehicleTrack saveVehicleTrack(VehicleTrack vehicleTrack) {
		logger.info("##VehicleTrackService##updateVehicleTrack: {}/{}", vehicleTrack.getVin(), vehicleTrack.getStatus());
		return vehicleTrackMongoRepository.save(vehicleTrack);
	}
	
	private Iterable<VehicleTrackCache> toVehicleTrackCache(List<Vehicle> vehicleList) {
		return vehicleList.stream()
							.map(VehicleTrackCacheMapper.fromVehicle)
							.collect(Collectors.toList());
	}


}
