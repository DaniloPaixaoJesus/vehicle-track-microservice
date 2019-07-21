package br.com.danilopaixao.vehicle.track.service;

import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;

@Service
public class VehicleTrackService {
	
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

	public String insertIntoQueue(String vin) {
		/**
		 * TODO:
		 * 	1 - consulta api de veiculo 
		 * 	2 - insert RabbitM 
		 */
		String generatedQueue = "12345";
		return "/api/v1/vehicle-track/"+generatedQueue+"/queue";
	}
	
	public String pushQueue(String vin) {
		/**
		 * TODO: 
		 * 1 - consulta dados do RabbitM
		 * 2 - atualiza Redis
		 * 3 - envia api rest para atualizar status 
		 */
		String generatedQueue = "12345";
		return "/api/v1/vehicle-track/"+generatedQueue+"/queue";
	}
}
