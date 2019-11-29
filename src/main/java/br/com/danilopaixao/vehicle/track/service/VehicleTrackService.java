package br.com.danilopaixao.vehicle.track.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackRedis;
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
		Iterable<VehicleTrackRedis> cache = vehicleTrackRedisRepository.findAll();
		if(cache == null) {
			logger.info("##Execution offline vehicle routine: NO VEHICLE");
			return;
		}
		logger.info("##Execution offline vehicle routine: {}", cache);
		cache.forEach(tracker -> {
				if(tracker.getStatus() == StatusEnum.ON) {
					if(tracker.isOffLineVehicle(secondsToOfffline)) {
						logger.info("vin status OFF:"+tracker.getVin());
						tracker.setStatus(StatusEnum.OFF);
						tracker.setDtStatus(ZonedDateTime.now());
						this.updateVehicleTrack(tracker);
						vehicleService.updateVehicle(tracker.getVin(), StatusEnum.OFF);
					}
				}
			});
	}

	public List<VehicleTrack> findNearest(double latitude, double longitude, double distance) {
		return vehicleTrackMongoRepository2.findNearest(latitude, longitude, distance);
	}
	
	public VehicleTrackRedis insertIntoQueueOnlineStatus(String vin, double[] position) throws Throwable {
		logger.info("##VehicleTrackService##insertIntoQueue: {} : {}", position);
		return vehicleTrackQueueSender.sendToQueueOnlineStatus(vin, position);
	}
	
	public VehicleTrackRedis insertVehicleTrack(VehicleTrackRedis vehicleTrack) {
		logger.info("##VehicleTrackService##insertVehicleTrack: {}", vehicleTrack.getVin());
		vehicleTrackMongoRepository.save(toVehicleTrack(vehicleTrack));
		return vehicleTrackRedisRepository.save(vehicleTrack);
	}
	
	private VehicleTrack toVehicleTrack(VehicleTrackRedis vehicleTrackRedis) {
		return new VehicleTrack(vehicleTrackRedis.getVin(), 
				vehicleTrackRedis.getQueue(), 
				vehicleTrackRedis.getStatus(), 
				vehicleTrackRedis.getDtIniStatus(), 
				vehicleTrackRedis.getDtIniStatus(),
				vehicleTrackRedis.getGeolocation());
	}
	
	public VehicleTrackRedis getVehicleTrack(String vin) {
		logger.info("##VehicleTrackService##getVehicleTrack: {}", vin);
		//TODO: use Optional class
		return vehicleTrackRedisRepository.findById(vin).orElse(null);
	}
	
	public Iterable<VehicleTrackRedis> findAll() {
		return vehicleTrackRedisRepository.findAll();
	}
	
	public Iterable<VehicleTrackRedis> getAllVehicleTrack() {
		return vehicleTrackRedisRepository.findAll();
	}

	public VehicleTrackRedis updateVehicleTrack(VehicleTrackRedis vehicleTrack) {
		logger.info("##VehicleTrackService##updateVehicleTrack: {}/{}", vehicleTrack.getVin(), vehicleTrack.getStatus());
		vehicleTrackMongoRepository.save(toVehicleTrack(vehicleTrack));
		return vehicleTrackRedisRepository.save(vehicleTrack);
	}
}
