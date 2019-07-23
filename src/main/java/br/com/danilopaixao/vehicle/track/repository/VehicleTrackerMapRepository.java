package br.com.danilopaixao.vehicle.track.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;

public class VehicleTrackerMapRepository {
	
	private Map<String, VehicleTrack> vehicles;
	private int quantity = 0;
	
	public List<VehicleTrack> getAll() {
		if(this.quantity <= 0) {
			return null;
		}
		List<VehicleTrack> return_ = new ArrayList<VehicleTrack>();
		for (String key: vehicles.keySet()){
            VehicleTrack value = vehicles.get(key);  
            return_.add(value);
		}
		return return_;
	}
	
	public VehicleTrack save(VehicleTrack vehicles) {
		if(vehicles == null
				|| vehicles.getVin() == null) {
			return null;
		}
		if(this.vehicles.get(vehicles.getVin()) != null) {
			this.vehicles.put(vehicles.getVin(), vehicles);
		}
		return vehicles;
	}
	
	public VehicleTrackerMapRepository() {
		vehicles = new HashMap<String, VehicleTrack>();
	}

	public VehicleTrack put(String vin, VehicleTrack vehicle) {
		quantity++;
		vehicles.put(vin, vehicle);
		return vehicle;
	}
	
	public VehicleTrack get(String vin) {
		return vehicles.get(vin);
	}

}
