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

import br.com.danilopaixao.vehicle.test.builder.VehicleTrackTestBuilder;
import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.repository.VehicleTrackRepository;
import br.com.danilopaixao.vehicle.track.service.VehicleService;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTrackServiceTest {

	@InjectMocks
	private VehicleTrackService vehicleTrackService;
	
	@Mock
	private VehicleTrackRepository vehicleTrackRepository;
	
	@Mock
	private VehicleService vehicleService;
	
	private VehicleTrackTestBuilder vehicleTrackBuilder;
	
	@Before
    public void init() {
		vehicleTrackBuilder = new VehicleTrackTestBuilder();
    }
	
	@Test
	public void testOnlineVehiclesToOffline() throws Exception {
		int vehicleQuantity = 3;
		int minDateTimeBefore = -5;
		when(vehicleTrackRepository.findAll()).thenReturn(this.getListVehicleTrackRandomVehicle(vehicleQuantity, minDateTimeBefore, StatusEnum.ON));
		vehicleTrackService.processOffLineVehicle();
		verify(vehicleTrackRepository, times(vehicleQuantity)).save(any(VehicleTrack.class));
		verify(vehicleService, times(vehicleQuantity)).updateVehicle(any(String.class), any(StatusEnum.class));
	}
	
	@Test
	public void testKeepOfflineVehicles() throws Exception {
		int vehicleQuantity = 3;
		int minDateTimeBefore = -5;
		when(vehicleTrackRepository.findAll()).thenReturn(this.getListVehicleTrackRandomVehicle(vehicleQuantity, minDateTimeBefore, StatusEnum.OFF));
		vehicleTrackService.processOffLineVehicle();
		verify(vehicleTrackRepository, times(0)).save(any(VehicleTrack.class));
		verify(vehicleService, times(0)).updateVehicle(any(String.class), any(StatusEnum.class));
	}
	
	@Test
	public void testNotFoundVehiclesTrack() throws Exception {
		when(vehicleTrackRepository.findAll()).thenReturn(null);
		vehicleTrackService.processOffLineVehicle();
		verify(vehicleTrackRepository, times(0)).save(any(VehicleTrack.class));
		verify(vehicleService, times(0)).updateVehicle(any(String.class), any(StatusEnum.class));
	}
	
	private List<VehicleTrack> getListVehicleTrackRandomVehicle(int quantity, int minBefore, StatusEnum status){
		List<VehicleTrack> r = new ArrayList<VehicleTrack>();
		for (int i = 0; i < quantity; i++) {
			r.add(vehicleTrackBuilder.buildRandom(minBefore, status));
		}
		return r;
	}

}
