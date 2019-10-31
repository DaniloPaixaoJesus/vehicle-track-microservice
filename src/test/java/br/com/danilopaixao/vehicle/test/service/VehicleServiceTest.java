package br.com.danilopaixao.vehicle.test.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import br.com.danilopaixao.vehicle.test.builder.VehicleTestBuilder;
import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.service.VehicleService;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceTest {

	@InjectMocks
	private VehicleService vehicleService;
	
	@Mock
	private RestTemplate restTemplate;
	
	private VehicleTestBuilder vehicleBuilder;
	
	@Before
    public void init() {
		vehicleBuilder = new VehicleTestBuilder();
    }
	
	@Test
	public void testUpdateVehicle() throws Exception{
		vehicleService.updateVehicle(vehicleBuilder.buildRandom(StatusEnum.ON).getVin(), StatusEnum.ON);
	}
	
	@Test
	public void testgetVehicle() throws Exception{
		vehicleService.getVehicle(vehicleBuilder.buildRandom(StatusEnum.ON).getVin());
	}

}
