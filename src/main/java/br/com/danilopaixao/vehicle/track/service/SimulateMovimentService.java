package br.com.danilopaixao.vehicle.track.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Location;

@Service
public class SimulateMovimentService {

	public static String STATUS_RUNNER = "OFF";

	@Autowired
	private VehicleSocketService vehicleWebSocketService;

	// sec min hour day-month month day-week (optional year which I left out)
	@Scheduled(cron = "0/5 * * * * ?")
	public void run() throws Exception {
		if (STATUS_RUNNER.equals("ON")) {
			System.out.println("EXECUTOU!");
			List<Location> locations = getLocations();
			for (Location location : locations) {
				Thread.sleep(5000);
				vehicleWebSocketService.updateStatusWebSocket("YS2R4X20005388011", StatusEnum.ON, location);			
			}
		}
	}

	private List<Location> getLocations() {
		List<Location> locations = new ArrayList<Location>();
		locations.add(new Location(-23.528195, -46.558302));
		locations.add(new Location(-23.529100, -46.559868));
		locations.add(new Location(-23.530871, -46.563516));
		locations.add(new Location(-23.532150, -46.567872));
		locations.add(new Location(-23.532337, -46.569320));
		locations.add(new Location(-23.530114, -46.569974));
		locations.add(new Location(-23.529018, -46.570089));
		locations.add(new Location(-23.528300, -46.567085));
		locations.add(new Location(-23.527916, -46.566211));
		locations.add(new Location(-23.527257, -46.565369));
		locations.add(new Location(-23.526465, -46.563878));
		locations.add(new Location(-23.525757, -46.562993));
		locations.add(new Location(-23.525301, -46.562537));
		locations.add(new Location(-23.523856, -46.558680));
		locations.add(new Location(-23.522203, -46.556202));
		locations.add(new Location(-23.519620, -46.555012));
		locations.add(new Location(-23.516992, -46.554741));
		locations.add(new Location(-23.515452, -46.550777));
		locations.add(new Location(-23.514297, -46.544125));
		locations.add(new Location(-23.514721, -46.538066));
		locations.add(new Location(-23.515713, -46.535876));
		locations.add(new Location(-23.518616, -46.535481));
		locations.add(new Location(-23.521252, -46.535095));
		locations.add(new Location(-23.522885, -46.535766));
		locations.add(new Location(-23.522093, -46.539312));
		return locations;
	}

}
