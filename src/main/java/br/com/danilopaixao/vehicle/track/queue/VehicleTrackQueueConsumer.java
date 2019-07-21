package br.com.danilopaixao.vehicle.track.queue;

import java.util.Date;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.service.VehicleService;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;
 
@Component
public class VehicleTrackQueueConsumer {
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private VehicleTrackService vehicleTrackService;
	
    @RabbitListener(queues = {"${queue.vehicle.track.name}"})
    public void receive(@Payload String payload) {
    	ObjectMapper jsonMapper = new ObjectMapper();
    	VehicleTrack vehicleTrackPayload = null;
      	try {
      		vehicleTrackPayload = jsonMapper.readValue(payload, VehicleTrack.class);
      		
      		VehicleTrack vehicleTrack = vehicleTrackService.getVehicleTrack(vehicleTrackPayload.getVin());
        	if (vehicleTrack == null) {
        		vehicleTrack = new VehicleTrack(vehicleTrackPayload.getVin(), "", "ON", new Date());
        		vehicleTrackService.insertVehicleTrack(vehicleTrack);
        		vehicleService.updateVehicle(vehicleTrackPayload.getVin(), "ON");
        	}else if (vehicleTrack.getStatus().equalsIgnoreCase("OFF")){
        		vehicleTrack.setStatus("ON");
        		vehicleTrackService.updateVehicleTrack(vehicleTrack);
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();
			vehicleService.updateVehicle(vehicleTrackPayload.getVin(), "ON");
		}
    }
}
