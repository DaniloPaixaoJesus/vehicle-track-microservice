package br.com.danilopaixao.vehicle.track.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;

@Service
public class VehicleTrackService {
	
	@Autowired
	private VehicleTrackQueueSender vehicleTrackQueueSender;
	
	public VehicleTrack getQueue(final String queue) {
		/**
		 * TODO: 
		 * 1 - consulta mensageria para verificar o status de processamento
		 * 2 - converte os dados para VehicleTrack
		 * 3 - caso ja esteja processado, consultar o Redis
		 */
		return new VehicleTrack("YS2R4X20005399401", "12345", "ON");
		//return new VehicleTrack("YS2R4X20005399401", "12345", "OFF");
	}

	public VehicleTrack insertIntoQueue(String vin) throws Throwable {
		return vehicleTrackQueueSender.sendQueue(vin);
	}
}
