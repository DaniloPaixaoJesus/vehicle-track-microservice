package br.com.danilopaixao.vehicle.test.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danilopaixao.vehicle.test.builder.VehicleTestBuilder;
import br.com.danilopaixao.vehicle.test.builder.VehicleTrackTestBuilder;
import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Location;
import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueConsumer;
import br.com.danilopaixao.vehicle.track.service.VehicleService;
import br.com.danilopaixao.vehicle.track.service.VehicleSocketService;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTrackQueueConsumerTest {

	@InjectMocks
	private VehicleTrackQueueConsumer vehicleTrackQueueConsumer;
	
	@Mock
	private VehicleService vehicleService;
	
	@Mock
	private VehicleTrackService vehicleTrackService;
	
	@Mock
	private VehicleSocketService vehicleSocketService;
	
	private String queueVehicleTrackName = "nome-da-fila";
	
	private VehicleTestBuilder vehicleBuilder;
	
	private VehicleTrackTestBuilder vehicleTrackBuilder;
	
	static private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Before
    public void init() {
		vehicleBuilder = new VehicleTestBuilder();
		vehicleTrackBuilder = new VehicleTrackTestBuilder();
		Whitebox.setInternalState(vehicleTrackQueueConsumer, "queueVehicleTrackName", queueVehicleTrackName);
    }
	
	@Test
	public void testNoVehicle() throws Exception{
		when(vehicleTrackService.getVehicleTrackCache(any(String.class))).thenReturn(null);
		when(vehicleService.getVehicle(any(String.class))).thenReturn(null);
		
		vehicleTrackQueueConsumer.receive(getJson(vehicleTrackBuilder.buildRandom(0, 0, StatusEnum.ON)));
		
		verify(vehicleTrackService, times(1)).getVehicleTrackCache(any(String.class));
		verify(vehicleService, times(1)).getVehicle(any(String.class));
		verify(vehicleTrackService, times(0)).insertVehicleTrackCache(any(VehicleTrackCache.class));
		verify(vehicleTrackService, times(0)).saveVehicleTrackCache(any(VehicleTrackCache.class));
		verify(vehicleService, times(0)).updateVehicleStatus(any(String.class), any(StatusEnum.class), any(Location.class));
	}
	
	@Test
	public void testOnlineVehicle() throws Exception{
		Vehicle vehicleFound = vehicleBuilder.buildRandom(StatusEnum.ON);
		VehicleTrackCache vehicleTrackFound = vehicleTrackBuilder.buildRandom(0, 0, StatusEnum.ON);
		vehicleTrackFound.setVin(vehicleFound.getVin());
		when(vehicleTrackService.getVehicleTrackCache(vehicleFound.getVin())).thenReturn(vehicleTrackFound);
		
		vehicleTrackQueueConsumer.receive(getJson(vehicleTrackFound));

		verify(vehicleTrackService, times(0)).insertVehicleTrackCache(any(VehicleTrackCache.class));
		verify(vehicleTrackService, times(1)).saveVehicleTrackCache(any(VehicleTrackCache.class));
		verify(vehicleService, times(1)).updateVehicleStatus(any(String.class), any(StatusEnum.class), any(Location.class));
	}
	
	@Test
	public void testOfflineVehicleNoCache() throws Exception{
		Vehicle vehicleFound = vehicleBuilder.buildRandom(StatusEnum.OFF);
		when(vehicleService.getVehicle(vehicleFound.getVin())).thenReturn(vehicleFound);
		when(vehicleTrackService.getVehicleTrackCache(any(String.class))).thenReturn(null);
		
		VehicleTrackCache vehicleTrackPayload = vehicleTrackBuilder.setVin(vehicleFound.getVin()).build();
		vehicleTrackQueueConsumer.receive(getJson(vehicleTrackPayload));
		
		verify(vehicleTrackService, times(1)).insertVehicleTrackCache(any(VehicleTrackCache.class));
		verify(vehicleService, times(1)).updateVehicleStatus(vehicleTrackPayload.getVin(), StatusEnum.ON, vehicleTrackPayload.getGeolocation());
		verify(vehicleService, times(0)).updateVehicleStatus(vehicleTrackPayload.getVin(), StatusEnum.OFF, vehicleTrackPayload.getGeolocation());
		verify(vehicleTrackService, times(0)).saveVehicleTrackCache(any(VehicleTrackCache.class));
	}
	
	@Test
	public void testOfflineVehicleInCache() throws Exception{
		Vehicle vehicleFound = vehicleBuilder.buildRandom(StatusEnum.ON);
		VehicleTrackCache vehicleTrackFournd = vehicleTrackBuilder.buildRandom(0, 0, StatusEnum.OFF);
		vehicleTrackFournd.setVin(vehicleFound.getVin());
		when(vehicleTrackService.getVehicleTrackCache(vehicleTrackFournd.getVin())).thenReturn(vehicleTrackFournd);
		
		vehicleTrackQueueConsumer.receive(getJson(
												new VehicleTrackTestBuilder()
														.setVin(vehicleFound.getVin())
														.build()));
		
		verify(vehicleTrackService, times(0)).insertVehicleTrackCache(vehicleTrackFournd);
		//verify(vehicleService, times(1)).updateVehicleStatus(vehicleTrackFournd.getVin(), StatusEnum.ON, any(Location.class));
		verify(vehicleTrackService, times(1)).saveVehicleTrackCache(vehicleTrackFournd);
	}
	
	private String getJson(VehicleTrackCache v) throws Exception {
		return jsonMapper.writeValueAsString(v);
	}
	

}
