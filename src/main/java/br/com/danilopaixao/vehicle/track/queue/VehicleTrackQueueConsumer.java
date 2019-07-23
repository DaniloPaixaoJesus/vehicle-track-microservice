package br.com.danilopaixao.vehicle.track.queue;

import java.util.Date;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.track.model.Vehicle;
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
    	
    	System.out.println("###### VehicleTrackQueueConsumer#receive#"+payload);
    	
    	ObjectMapper jsonMapper = new ObjectMapper();
    	VehicleTrack vehicleTrackPayload = null;
      	try {
      		vehicleTrackPayload = jsonMapper.readValue(payload, VehicleTrack.class);
      		
      		Vehicle vehicle = vehicleService.getVehicle(vehicleTrackPayload.getVin());
        	if( vehicle == null
        			|| vehicle.getVin() == null
        			|| vehicle.getVin().isEmpty()) {
        		return;
        	}
      		
      		VehicleTrack vehicleTrackCache = vehicleTrackService.getVehicleTrack(vehicleTrackPayload.getVin());
      		
        	if (vehicleTrackCache == null) {
        		vehicleTrackCache = new VehicleTrack(vehicleTrackPayload.getVin(), "", "ON", new Date());
        		vehicleTrackService.insertVehicleTrack(vehicleTrackCache);
        		vehicleService.updateVehicle(vehicleTrackPayload.getVin(), "ON");
        	}else if (vehicleTrackCache.getStatus().equalsIgnoreCase("OFF")){
        		vehicleTrackCache.setStatus("ON");
        		vehicleTrackService.updateVehicleTrack(vehicleTrackCache);
        		vehicleService.updateVehicle(vehicleTrackPayload.getVin(), "ON");
        	}
        	
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
    }
}
