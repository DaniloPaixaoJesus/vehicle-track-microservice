package br.com.danilopaixao.vehicle.track.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackRepository;

@Service
public class VehicleTrackService {
	
	@Autowired
	private VehicleTrackRepository vehicleTrackRepository;
	
	@Autowired
	private VehicleTrackQueueSender vehicleTrackQueueSender;
	
	@Autowired
	private VehicleService vehicleService;
	
	public VehicleTrack getQueue(final String queue) {
		/**
		 * TODO: 
		 * 1 - consulta mensageria para verificar o status de processamento
		 * 2 - converte os dados para VehicleTrack
		 * 3 - caso ja esteja processado, consultar o Redis
		 */
		return new VehicleTrack("YS2R4X20005399401", "12345", "ON", new Date());
		//return new VehicleTrack("YS2R4X20005399401", "12345", "OFF");
	}
	
	@Scheduled(cron = "0/59 * * * * ?")
	public void processOffLineVehicle() {
		System.out.println(" -- EXECUTOU PROGRAMA --");
		vehicleTrackRepository
			.findAll()
			.forEach(tracker -> {
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
		return vehicleTrackRepository.save(vehicleTrack);
	}
	
	public Iterable<VehicleTrack> getAllVehicleTrack() {
		return vehicleTrackRepository.findAll();
	}
	
	public VehicleTrack getVehicleTrack(String vin) {
		return vehicleTrackRepository.findById(vin).orElse(null);
	}
	
	public VehicleTrack updateVehicleTrack(VehicleTrack vehicleTrack) {
		return vehicleTrackRepository.save(vehicleTrack);
	}
}
