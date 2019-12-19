package br.com.danilopaixao.vehicle.test.service;


import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import br.com.danilopaixao.vehicle.test.builder.VehicleTestBuilder;
import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Location;
import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.queue.VehicleTrackQueueSender;
import br.com.danilopaixao.vehicle.track.service.VehicleService;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceTest {

	@InjectMocks
	private VehicleService vehicleService;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private VehicleTrackQueueSender vehicleTrackQueueSender;
	
	private VehicleTestBuilder vehicleBuilder;
	
	@Before
    public void init() {
		vehicleBuilder = new VehicleTestBuilder();
    }
	
	@Test
	public void testUpdateVehicleON() throws Exception{
		vehicleService.updateVehicleStatus(vehicleBuilder.buildRandom(StatusEnum.ON).getVin(), StatusEnum.ON, new Location());
	}
	
	@Test
	public void testUpdateVehicleOFF() throws Exception{
		vehicleService.updateVehicleStatus(vehicleBuilder.buildRandom(StatusEnum.ON).getVin(), StatusEnum.OFF, new Location());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNullVinVehicle() throws Exception{
		vehicleService.updateVehicleStatus(null, StatusEnum.OFF, new Location());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNullStatusVehicle() throws Exception{
		vehicleService.updateVehicleStatus(vehicleBuilder.buildRandom(StatusEnum.ON).getVin(), null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNullVehicle() throws Exception{
		vehicleService.updateVehicleStatus(null, null, null);
	}
	
	@Test
	public void testgetVehicle() throws Exception{
		vehicleService.getVehicle(vehicleBuilder.buildRandom(StatusEnum.ON).getVin());
	}
	
	@Test
	public void testgetVehicleNull() throws Exception{
		Vehicle v = vehicleService.getVehicle(null);
		assertNull(v);
	}
	
	@Test
	public void testgetVehicleEmpity() throws Exception{
		Vehicle v = vehicleService.getVehicle("");
		assertNull(v);
	}

}
