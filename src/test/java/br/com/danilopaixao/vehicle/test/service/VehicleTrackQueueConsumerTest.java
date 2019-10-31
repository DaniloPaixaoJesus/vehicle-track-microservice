package br.com.danilopaixao.vehicle.test.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.test.builder.VehicleTestBuilder;
import br.com.danilopaixao.vehicle.test.builder.VehicleTrackTestBuilder;
import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueConsumer;
import br.com.danilopaixao.vehicle.track.service.VehicleService;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTrackQueueConsumerTest {

	@InjectMocks
	private VehicleTrackQueueConsumer vehicleTrackQueueConsumer;
	
	@Mock
	private VehicleService vehicleService;
	
	@Mock
	private VehicleTrackService vehicleTrackService;
	
	private VehicleTestBuilder vehicleBuilder;
	
	private VehicleTrackTestBuilder vehicleTrackBuilder;
	
	static private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Before
    public void init() {
		vehicleBuilder = new VehicleTestBuilder();
		vehicleTrackBuilder = new VehicleTrackTestBuilder();
    }
	
	@Test
	public void testNoVehicle() throws Exception{
		when(vehicleService.getVehicle(any(String.class))).thenReturn(null);
		
		vehicleTrackQueueConsumer.receive(getJson(vehicleTrackBuilder.buildRandom(0, StatusEnum.ON)));
		
		verify(vehicleTrackService, times(0)).getVehicleTrack(any(String.class));
		verify(vehicleTrackService, times(0)).insertVehicleTrack(any(VehicleTrack.class));
		verify(vehicleTrackService, times(0)).updateVehicleTrack(any(VehicleTrack.class));
		verify(vehicleService, times(0)).updateVehicle(any(String.class), any(StatusEnum.class));
	}
	
	@Test
	public void testOnlineVehicle() throws Exception{
		when(vehicleService.getVehicle(any(String.class))).thenReturn(vehicleBuilder.buildRandom(StatusEnum.ON));
		when(vehicleTrackService.getVehicleTrack(any(String.class))).thenReturn(vehicleTrackBuilder.buildRandom(0, StatusEnum.ON));
		
		vehicleTrackQueueConsumer.receive(getJson(vehicleTrackBuilder.buildRandom(0, StatusEnum.ON)));

		verify(vehicleTrackService, times(0)).insertVehicleTrack(any(VehicleTrack.class));
		verify(vehicleTrackService, times(0)).updateVehicleTrack(any(VehicleTrack.class));
		verify(vehicleService, times(0)).updateVehicle(any(String.class), any(StatusEnum.class));
	}
	
	@Test
	public void testOfflineVehicleNoCache() throws Exception{
		when(vehicleService.getVehicle(any(String.class))).thenReturn(vehicleBuilder.buildRandom(StatusEnum.ON));
		when(vehicleTrackService.getVehicleTrack(any(String.class))).thenReturn(null);
		
		vehicleTrackQueueConsumer.receive(getJson(vehicleTrackBuilder.buildRandom(0, StatusEnum.ON)));
		
		verify(vehicleTrackService, times(1)).insertVehicleTrack(any(VehicleTrack.class));
		verify(vehicleService, times(1)).updateVehicle(any(String.class), any(StatusEnum.class));
		verify(vehicleTrackService, times(0)).updateVehicleTrack(any(VehicleTrack.class));
	}
	
	@Test
	public void testOfflineVehicleInCache() throws Exception{
		when(vehicleService.getVehicle(any(String.class))).thenReturn(vehicleBuilder.buildRandom(StatusEnum.ON));
		when(vehicleTrackService.getVehicleTrack(any(String.class))).thenReturn(vehicleTrackBuilder.buildRandom(0, StatusEnum.OFF));
		
		vehicleTrackQueueConsumer.receive(getJson(vehicleTrackBuilder.buildRandom(0, StatusEnum.ON)));
		
		verify(vehicleTrackService, times(0)).insertVehicleTrack(any(VehicleTrack.class));
		verify(vehicleService, times(1)).updateVehicle(any(String.class), any(StatusEnum.class));
		verify(vehicleTrackService, times(1)).updateVehicleTrack(any(VehicleTrack.class));
	}
	
	private String getJson(VehicleTrack v) throws Exception {
		return jsonMapper.writeValueAsString(v);
	}
	

}
