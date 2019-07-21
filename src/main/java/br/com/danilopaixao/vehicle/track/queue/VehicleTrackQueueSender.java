package br.com.danilopaixao.vehicle.track.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.service.VehicleService;
 
@Component
public class VehicleTrackQueueSender {
 
    @Autowired
    private RabbitTemplate rabbitTemplate;
 
    @Autowired
	private VehicleService vehicleService;
    
    @Autowired
    private Queue queue;
 
    public VehicleTrack sendQueue(String vin) throws JsonProcessingException {
    	
    	Vehicle vehicle = vehicleService.getVehicle(vin);
    	if( vehicle == null
    			|| vehicle.getVin() == null
    			|| vehicle.getVin().isEmpty()) {
    		return null;
    	}
    	ObjectMapper jsonMapper = new ObjectMapper();
    	VehicleTrack vehicleTrack = new VehicleTrack(vehicle.getVin(), "", "ON");
    	String payload = jsonMapper.writeValueAsString(vehicleTrack);
        rabbitTemplate.convertAndSend(this.queue.getName(), payload);
        return vehicleTrack;
    }
}