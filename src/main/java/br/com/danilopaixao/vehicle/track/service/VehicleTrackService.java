package br.com.danilopaixao.vehicle.track.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackerMapRepository;

@Service
public class VehicleTrackService {
	
	@Autowired
	private VehicleTrackerMapRepository vehicleTrackRepository;
	
	@Autowired
	private VehicleTrackQueueSender vehicleTrackQueueSender;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Scheduled(cron = "0/59 * * * * ?")
	public void processOffLineVehicle() {
		List<VehicleTrack> cache = vehicleTrackRepository.getAll();
		if(cache == null) {
			return;
		}
		cache.forEach(tracker -> {
				if(tracker.getStatus().equalsIgnoreCase("ON")) {
					if(setOffLineVehicle(tracker.getDtStatus())) {
						System.out.println("PASSOU DE 1 MIN - MUDA PRA OFFLINE");
						tracker.setStatus("OFF");
						tracker.setDtStatus(new Date());
						this.updateVehicleTrack(tracker);
						vehicleService.updateVehicle(tracker.getVin(), "OFF");
					}
				}
			});
	}
	
	public boolean setOffLineVehicle(Date lastChangeStatus) {
		try {
			Date d2 = new Date();
			//in milliseconds
			long diff = d2.getTime() - lastChangeStatus.getTime();

			long diffSeconds = diff / 1000 % 60;
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
	
	public VehicleTrack insertIntoQueue(String vin) throws Throwable {
		return vehicleTrackQueueSender.sendQueue(vin);
	}
	
	public VehicleTrack insertVehicleTrack(VehicleTrack vehicleTrack) {
		return vehicleTrackRepository.put(vehicleTrack.getVin(), vehicleTrack);
	}
	
	public VehicleTrack getVehicleTrack(String vin) {
		return vehicleTrackRepository.get(vin);
	}
	
	public List<VehicleTrack> getAllVehicleTrack() {
		return vehicleTrackRepository.getAll();
	}

	public VehicleTrack updateVehicleTrack(VehicleTrack vehicleTrack) {
		return vehicleTrackRepository.save(vehicleTrack);
	}
}
