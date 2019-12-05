package br.com.danilopaixao.vehicle.track.queue;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Location;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;
 
@Component
public class VehicleTrackQueueSender {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleTrackQueueSender.class);
	
	@Value("${queue.vehicle.track.name}")
    private String vehicleTrackQueueName;
 
    @Autowired
    private RabbitTemplate rabbitTemplate;
 
    @Autowired
    private Queue queue;
 
    public VehicleTrackCache sendToQueueOnlineStatus(final String vin, double[] position ) throws JsonProcessingException {
    	logger.info("###### VehicleTrackQueueSender#sendQueue:{}", vin);
    	
    	ObjectMapper jsonMapper = new ObjectMapper();
    	VehicleTrackCache vehicleTrackCache = new VehicleTrackCache(vin, vehicleTrackQueueName, 
    																StatusEnum.ON, 
    																LocalDateTime.now(), 
    																new Location(position[0], position[1]));
    	 String payload = jsonMapper.writeValueAsString(vehicleTrackCache);
        rabbitTemplate.convertAndSend(this.queue.getName(), payload);
        return vehicleTrackCache;
    }
}