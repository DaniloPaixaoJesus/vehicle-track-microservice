package br.com.danilopaixao.vehicle.track.service;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackRepository;

@Service
public class VehicleTrackService {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleTrackService.class);	
	
	@Value("${br.com.danilopaixao.vehicle.track.secondstooffline}")
	private int secondsToOfffline;
	
	@Autowired
	private VehicleTrackRepository vehicleTrackRepository;
	
	@Autowired
	private VehicleTrackQueueSender vehicleTrackQueueSender;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Scheduled(cron = "0/59 * * * * ?")
	public void processOffLineVehicle() {
		Iterable<VehicleTrack> cache = vehicleTrackRepository.findAll();
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
	
	public VehicleTrack insertIntoQueueOnlineStatus(String vin) throws Throwable {
		logger.info("##VehicleTrackService##insertIntoQueue: {}", vin);
		return vehicleTrackQueueSender.sendToQueueOnlineStatus(vin);
	}
	
	public VehicleTrack insertVehicleTrack(VehicleTrack vehicleTrack) {
		logger.info("##VehicleTrackService##insertVehicleTrack: {}", vehicleTrack.getVin());
		return vehicleTrackRepository.save(vehicleTrack);
	}
	
	public VehicleTrack getVehicleTrack(String vin) {
		logger.info("##VehicleTrackService##getVehicleTrack: {}", vin);
		//TODO: use Optional class
		return vehicleTrackRepository.findById(vin).orElse(null);
	}
	
	public Iterable<VehicleTrack> findAll() {
		return vehicleTrackRepository.findAll();
	}
	
	public Iterable<VehicleTrack> getAllVehicleTrack() {
		return vehicleTrackRepository.findAll();
	}

	public VehicleTrack updateVehicleTrack(VehicleTrack vehicleTrack) {
		logger.info("##VehicleTrackService##updateVehicleTrack: {}/{}", vehicleTrack.getVin(), vehicleTrack.getStatus());
		return vehicleTrackRepository.save(vehicleTrack);
	}
}
