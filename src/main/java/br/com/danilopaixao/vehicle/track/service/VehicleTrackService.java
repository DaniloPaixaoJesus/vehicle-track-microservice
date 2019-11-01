package br.com.danilopaixao.vehicle.track.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackRepository;

@Service
public class VehicleTrackService {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleTrackService.class);	
	
	@Autowired
	private VehicleTrackRepository vehicleTrackRepository;
	
//	@Autowired
//	private VehicleTrackMapRepository vehicleTrackMapRepository;
	
	@Autowired
	private VehicleTrackQueueSender vehicleTrackQueueSender;
	
	@Autowired
	private VehicleService vehicleService;
	
	public Iterable<VehicleTrack> testRedis() {
		return vehicleTrackRepository.findAll();
	}
	
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
					if(setOffLineVehicle(tracker.getDtStatus())) {
						logger.info("vin status OFF:"+tracker.getVin());
						tracker.setStatus(StatusEnum.OFF);
						tracker.setDtStatus(new Date());
						this.updateVehicleTrack(tracker);
						vehicleService.updateVehicle(tracker.getVin(), StatusEnum.OFF);
					}
				}
			});
	}
	
	public boolean setOffLineVehicle(Date lastChangeStatus) {
		try {
			if(lastChangeStatus == null) {
				return true;
			}
			Date d2 = new Date();
			long diff = d2.getTime() - lastChangeStatus.getTime();
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			if(diffDays == 0L
					&& diffHours == 0L
					&& diffMinutes < 1L
					) {
				return false;
			}else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

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
	
	public Iterable<VehicleTrack> getAllVehicleTrack() {
		return vehicleTrackRepository.findAll();
	}

	public VehicleTrack updateVehicleTrack(VehicleTrack vehicleTrack) {
		logger.info("##VehicleTrackService##updateVehicleTrack: {}/{}", vehicleTrack.getVin(), vehicleTrack.getStatus());
		return vehicleTrackRepository.save(vehicleTrack);
	}
}
