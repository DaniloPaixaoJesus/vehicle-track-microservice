package br.com.danilopaixao.vehicle.track.queue;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Location;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;
 
@Component
public class VehicleTrackQueueSender {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleTrackQueueSender.class);
	
    @Autowired
    private RabbitTemplate rabbitTemplate;
 
    @Value("${queue.vehicle.track}")
    private String vehicleTrackQueueName;
    
    @Value("${queue.vehicle.service.update}")
    private String queueVehicleServiceUpdate;
    
//    @Autowired
//    @Qualifier("${queue.vehicle.track}")
//    private Queue queueVehicleTrack;
//    
//    @Autowired
//    @Qualifier("${queue.vehicle.service.update}")
//    private Queue queueVehicleServiceUpdate;
 
    public VehicleTrackCache sendToQueueOnlineStatus(final String vin, double[] position ) throws JsonProcessingException {
    	logger.info("###### VehicleTrackQueueSender#sendQueue:{}", vin);
    	ObjectMapper jsonMapper = new ObjectMapper();
    	VehicleTrackCache vehicleTrackCache = new VehicleTrackCache(vin, vehicleTrackQueueName, 
    																StatusEnum.ON, 
    																LocalDateTime.now(), 
    																new Location(position[0], position[1]));
    	 String payload = jsonMapper.writeValueAsString(vehicleTrackCache);
        rabbitTemplate.convertAndSend(vehicleTrackQueueName, payload);
        return vehicleTrackCache;
    }
    
    public void sendToVehicleServiceQueue(final String vin, StatusEnum status, Location location, LocalDateTime dateTime) throws JsonProcessingException{
    	logger.info("###### VehicleTrackQueueSender#sendToVehicleServiceQueue:{}, {}, {}", vin, status, location);
    	ObjectMapper jsonMapper = new ObjectMapper();
    	VehicleTrack vehicleTrack = new VehicleTrack(vin, vehicleTrackQueueName, 
    																status, 
    																dateTime, 
    																location);
    	 String payload = jsonMapper.writeValueAsString(vehicleTrack);
    	 rabbitTemplate.convertAndSend(queueVehicleServiceUpdate, payload);
    }
}