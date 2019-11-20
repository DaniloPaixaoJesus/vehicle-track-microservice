package br.com.danilopaixao.vehicle.track.queue;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
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
    	VehicleTrack vehicleTrackPayload = null;
      	try {
      		vehicleTrackPayload = jsonMapper.readValue(payload, VehicleTrack.class);
      		
      		Vehicle vehicle = vehicleService.getVehicle(vehicleTrackPayload.getVin());
        	if( vehicle == null
        			|| vehicle.getVin() == null
        			|| vehicle.getVin().isEmpty()) {
        		logger.warn("###### VehicleTrackQueueConsumer# vehicle notfoud, vin {}", vehicleTrackPayload);
        		return;
        	}
      		
      		VehicleTrack vehicleTrackCache = vehicleTrackService.getVehicleTrack(vehicleTrackPayload.getVin());
      		
        	if (vehicleTrackCache == null) {
        		logger.info("###### VehicleTrackQueueConsumer#receive - insert cache database first time: {}", vehicleTrackPayload.getVin());
        		vehicleTrackCache = new VehicleTrack(vehicleTrackPayload.getVin(), queueVehicleTrackName, StatusEnum.ON, ZonedDateTime.now(), null);
        		vehicleService.updateVehicle(vehicleTrackPayload.getVin(), StatusEnum.ON);
        		vehicleTrackService.insertVehicleTrack(vehicleTrackCache);
        		vehicleSocketService.updateStatusWebSocket(vehicleTrackPayload.getVin(), StatusEnum.ON);
        	}else if (vehicleTrackCache.getStatus() == StatusEnum.OFF){
        		logger.info("###### VehicleTrackQueueConsumer#receive - update cache database, vin {}, status {}", vehicleTrackPayload.getVin(), StatusEnum.ON);
        		vehicleTrackCache.setStatus(StatusEnum.ON);
        		vehicleService.updateVehicle(vehicleTrackPayload.getVin(), StatusEnum.ON);
        		vehicleTrackService.updateVehicleTrack(vehicleTrackCache);
        		vehicleSocketService.updateStatusWebSocket(vehicleTrackPayload.getVin(), StatusEnum.ON);
        	}
        	logger.info("###### VehicleTrackQueueConsumer#receive: end of process");        	
		} catch (Exception e) {
			logger.error("###### VehicleTrackQueueConsumer# error", e);
			return;
		}
    }
}
