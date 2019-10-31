package br.com.danilopaixao.vehicle.track.service;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackMapRepository;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackRepository;

@Service
public class VehicleTrackService {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleTrackService.class);	
	
	@Autowired
	private VehicleTrackRepository vehicleTrackRepository;
	
	@Autowired
	private VehicleTrackMapRepository vehicleTrackMapRepository;
	
	@Autowired
	private VehicleTrackQueueSender vehicleTrackQueueSender;
	
	@Autowired
	private VehicleService vehicleService;
	
	public Iterable<VehicleTrack> testRedis() {
		return vehicleTrackRepository.findAll();
	}
	
	@Scheduled(cron = "0/59 * * * * ?")
	public void processOffLineVehicle() {
		logger.info("##Execution offline vehicle routine");
		List<VehicleTrack> cache = vehicleTrackMapRepository.getAll();
		if(cache == null) {
			return;
		}
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
			//in milliseconds
			long diff = d2.getTime() - lastChangeStatus.getTime();

//			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

//			System.out.print(diffDays + " days, ");
//			System.out.print(diffHours + " hours, ");
//			System.out.print(diffMinutes + " minutes, ");
//			System.out.print(diffSeconds + " seconds.");
			
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
		return vehicleTrackMapRepository.put(vehicleTrack.getVin(), vehicleTrack);
	}
	
	public VehicleTrack getVehicleTrack(String vin) {
		logger.info("##VehicleTrackService##getVehicleTrack: {}", vin);
		return vehicleTrackMapRepository.get(vin);
	}
	
	public List<VehicleTrack> getAllVehicleTrack() {
		return vehicleTrackMapRepository.getAll();
	}

	public VehicleTrack updateVehicleTrack(VehicleTrack vehicleTrack) {
		logger.info("##VehicleTrackService##updateVehicleTrack: {}/{}", vehicleTrack.getVin(), vehicleTrack.getStatus());
		return vehicleTrackMapRepository.save(vehicleTrack);
	}
}
