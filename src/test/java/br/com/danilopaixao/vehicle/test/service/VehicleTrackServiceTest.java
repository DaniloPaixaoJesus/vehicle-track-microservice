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
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.danilopaixao.vehicle.test.builder.VehicleTrackTestBuilder;
import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackRedis;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackRedisRepository;
import br.com.danilopaixao.vehicle.track.service.VehicleService;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTrackServiceTest {

	@InjectMocks
	private VehicleTrackService vehicleTrackService;
	
	@Mock
	private VehicleTrackRedisRepository vehicleTrackRepository;
	
	@Mock
	private VehicleService vehicleService;
	
	private VehicleTrackTestBuilder vehicleTrackBuilder;
	
	@Before
    public void init() {
		vehicleTrackBuilder = new VehicleTrackTestBuilder();
    }
	
	@Test
	public void testOnlineVehiclesToOffline() throws Exception {
		int vehicleQuantity = 1;
		int minDateTimeBefore = -2;
		int minIniBefore = minDateTimeBefore * 10;
		List<VehicleTrackRedis> vehicleTrackListFound = this.getListVehicleTrackRandomVehicle(vehicleQuantity, 
																					minDateTimeBefore, 
																					minIniBefore, 
																					StatusEnum.ON);
		when(vehicleTrackRepository.findAll()).thenReturn(vehicleTrackListFound);
		Whitebox.setInternalState(vehicleTrackService, "secondsToOfffline", 60);
		vehicleTrackService.processOffLineVehicle();
		verify(vehicleTrackRepository, times(vehicleQuantity)).save(vehicleTrackListFound.get(0));
		verify(vehicleService, times(vehicleQuantity)).updateVehicle(vehicleTrackListFound.get(0).getVin(), StatusEnum.OFF);
	}
	
	@Test
	public void testKeepOnlineVehicles() throws Exception {
		int vehicleQuantity = 1;
		int minDateTimeBefore = -1;
		int minIniBefore = minDateTimeBefore * 10;
		List<VehicleTrackRedis> vehicleTrackListFound = this.getListVehicleTrackRandomVehicle(vehicleQuantity, 
				minDateTimeBefore, 
				minIniBefore, 
				StatusEnum.ON);
		when(vehicleTrackRepository.findAll()).thenReturn(vehicleTrackListFound);
		Whitebox.setInternalState(vehicleTrackService, "secondsToOfffline", 61);
		vehicleTrackService.processOffLineVehicle();
		verify(vehicleTrackRepository, times(0)).save(any(VehicleTrackRedis.class));
		verify(vehicleService, times(0)).updateVehicle(any(String.class), any(StatusEnum.class));
	}
	
	@Test
	public void testKeepOfflineVehicles() throws Exception {
		int vehicleQuantity = 1;
		int minDateTimeBefore = -5;
		int minIniBefore = minDateTimeBefore * 10;
		List<VehicleTrackRedis> vehicleTrackListFound = this.getListVehicleTrackRandomVehicle(vehicleQuantity, 
				minDateTimeBefore, 
				minIniBefore, 
				StatusEnum.OFF);
		when(vehicleTrackRepository.findAll()).thenReturn(vehicleTrackListFound);
		Whitebox.setInternalState(vehicleTrackService, "secondsToOfffline", 60);
		vehicleTrackService.processOffLineVehicle();
		verify(vehicleTrackRepository, times(0)).save(any(VehicleTrackRedis.class));
		verify(vehicleService, times(0)).updateVehicle(any(String.class), any(StatusEnum.class));
	}
	
	@Test
	public void testNotFoundVehiclesTrack() throws Exception {
		when(vehicleTrackRepository.findAll()).thenReturn(null);
		Whitebox.setInternalState(vehicleTrackService, "secondsToOfffline", 60);
		vehicleTrackService.processOffLineVehicle();
		verify(vehicleTrackRepository, times(0)).save(any(VehicleTrackRedis.class));
		verify(vehicleService, times(0)).updateVehicle(any(String.class), any(StatusEnum.class));
	}
	
	private List<VehicleTrackRedis> getListVehicleTrackRandomVehicle(int quantity, int minBefore, int minIniBefore, StatusEnum status){
		List<VehicleTrackRedis> r = new ArrayList<VehicleTrackRedis>();
		for (int i = 0; i < quantity; i++) {
			r.add(vehicleTrackBuilder.buildRandom(minBefore, minIniBefore, status));
		}
		return r;
	}

}
