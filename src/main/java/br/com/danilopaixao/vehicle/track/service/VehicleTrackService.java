package br.com.danilopaixao.vehicle.track.service;

import java.time.ZonedDateTime;
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
	
	@Scheduled(cron = "0/59 * * * * ?")
	public void processOffLineVehicle() {
		Iterable<VehicleTrackCache> vehicleTrackCache = this.findAllVehicleTrackCache();
		if(vehicleTrackCache == null) {
			VehicleList vehicleList = vehicleService.getAllVehicle();
			if( vehicleList == null
					|| vehicleList.getVehicleList() == null
					|| vehicleList.getVehicleList().isEmpty()) {
				logger.info("##Execution offline vehicle routine: NO VEHICLE");
				return;	
			}
			vehicleTrackCache = toVehicleTrackCache(vehicleList.getVehicleList());
		}
		logger.info("##Execution offline vehicle routine - vehicles found ...");
		vehicleTrackCache.forEach(tracker -> {
				if(tracker.getStatus() == StatusEnum.ON) {
					if(tracker.isOffLineVehicle(secondsToOfffline)) {
						logger.info("vin status OFF:"+tracker.getVin());
						tracker.setStatus(StatusEnum.OFF);
						tracker.setDtStatus(ZonedDateTime.now());
						this.saveVehicleTrackCache(tracker);
						vehicleService.updateVehicle(tracker.getVin(), StatusEnum.OFF);
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
	
//	private VehicleTrack toVehicleTrack(VehicleTrackCache vehicleTrackRedis) {
//		return new VehicleTrack(vehicleTrackRedis.getVin(), 
//				vehicleTrackRedis.getQueue(), 
//				vehicleTrackRedis.getStatus(), 
//				vehicleTrackRedis.getDtIniStatus(), 
//				vehicleTrackRedis.getDtIniStatus(),
//				vehicleTrackRedis.getGeolocation());
//	}
	
	public VehicleTrackCache getVehicleTrack(String vin) {
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
