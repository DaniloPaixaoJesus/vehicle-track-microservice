package br.com.danilopaixao.vehicle.track.queue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.service.VehicleService;
 
@Component
public class VehicleTrackQueueConsumer {
	
	@Autowired
	private VehicleService vehicleService;
	
    @RabbitListener(queues = {"${queue.vehicle.track.name}"})
    public void receive(@Payload String payload) {
    	ObjectMapper jsonMapper = new ObjectMapper();
      	try {
      		VehicleTrack vehicleTrack = jsonMapper.readValue(payload, VehicleTrack.class);
      		vehicleService.updateVehicle(vehicleTrack.getVin(), "ON");
      		System.out.println("===>vehicleTrack==>"+vehicleTrack);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
