package br.com.danilopaixao.vehicle.track.queue;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.mapper.VehicleTrackMapper;
import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;
import br.com.danilopaixao.vehicle.track.service.VehicleService;
import br.com.danilopaixao.vehicle.track.service.VehicleSocketService;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;
 
@Component
public class VehicleTrackQueueConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleTrackQueueConsumer.class);
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private VehicleTrackService vehicleTrackService;
	
	@Autowired
	private VehicleSocketService vehicleSocketService;
	
	@Value("${queue.vehicle.track.name}")
	private String queueVehicleTrackName;
	
    @RabbitListener(queues = {"${queue.vehicle.track.name}"})
    public void receive(@Payload String payload) {
    	
    	logger.info("###### VehicleTrackQueueConsumer#receive:{}", payload);
    	
    	ObjectMapper jsonMapper = new ObjectMapper();
    	VehicleTrackCache vehicleTrackPayload = null;
      	try {
      		vehicleTrackPayload = jsonMapper.readValue(payload, VehicleTrackCache.class);
      		
      		VehicleTrackCache vehicleTrackCache = vehicleTrackService.getVehicleTrackCache(vehicleTrackPayload.getVin());
      		
      		Vehicle vehicle = null;
      		if(vehicleTrackCache == null) {
      			vehicle = vehicleService.getVehicle(vehicleTrackPayload.getVin());
      			if( vehicle == null
            			|| vehicle.getVin() == null
            			|| vehicle.getVin().isEmpty()) {
            		logger.warn("###### VehicleTrackQueueConsumer# vehicle notfoud, vin {}", vehicleTrackPayload);
            		return;
            	}
      		}
        	
      		
        	if (vehicleTrackCache == null) {
        		logger.info("###### VehicleTrackQueueConsumer#receive - insert cache database first time: {}", vehicleTrackPayload.getVin());
        		vehicleTrackCache = new VehicleTrackCache(vehicleTrackPayload.getVin(), 
        													queueVehicleTrackName, 
        													StatusEnum.ON, 
        													LocalDateTime.now(), 
        													vehicleTrackPayload.getGeolocation());
        		
        		vehicleTrackService.insertVehicleTrackCache(vehicleTrackCache);
        		vehicleTrackService.insertVehicleTrack(VehicleTrackMapper.fromVehicleTrackCache(vehicleTrackCache));
        		vehicleSocketService.updateStatusWebSocket(vehicleTrackPayload.getVin(), StatusEnum.ON);
        		vehicleService.updateVehicleStatus(vehicleTrackPayload.getVin(), StatusEnum.ON, vehicleTrackPayload.getGeolocation());
        	}else{
        		logger.info("###### VehicleTrackQueueConsumer#receive - update cache database, vin {}, status {}", vehicleTrackPayload.getVin(), StatusEnum.ON);
        		vehicleTrackCache.setStatus(StatusEnum.ON);
        		vehicleTrackCache.setDtStatus(LocalDateTime.now());
        		vehicleTrackCache.setGeolocation(vehicleTrackPayload.getGeolocation());
        		
        		vehicleTrackService.saveVehicleTrackCache(vehicleTrackCache);
        		vehicleTrackService.insertVehicleTrack(VehicleTrackMapper.fromVehicleTrackCache(vehicleTrackCache));
        		vehicleSocketService.updateStatusWebSocket(vehicleTrackPayload.getVin(), StatusEnum.ON);
        		vehicleService.updateVehicleStatus(vehicleTrackPayload.getVin(), StatusEnum.ON, vehicleTrackPayload.getGeolocation());
        	}
        	logger.info("###### VehicleTrackQueueConsumer#receive: end of process");        	
		} catch (Exception e) {
			logger.error("###### VehicleTrackQueueConsumer# error", e);
			return;
		}
    }
}
