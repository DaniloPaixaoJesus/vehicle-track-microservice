package br.com.danilopaixao.vehicle.track.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;

public class VehicleTrackerMapRepository {
	
	public static void main(String[] args) {
		VehicleTrackerMapRepository staticVehicleTrackerMap = new VehicleTrackerMapRepository();
		staticVehicleTrackerMap.put("1234", new VehicleTrack("1234", "", "ON", new Date()));
		staticVehicleTrackerMap.put("5678", new VehicleTrack("1234", "", "ON", new Date()));
		staticVehicleTrackerMap.put("1234", new VehicleTrack("1234", "", "ON", new Date()));
		
		System.out.println(staticVehicleTrackerMap.get("1234"));
		System.out.println(staticVehicleTrackerMap.get("5678"));
		System.out.println(staticVehicleTrackerMap.get("123412"));
		
	}
	
	private Map<String, VehicleTrack> vehicles;
	private int quantity = 0;
	
	public List<VehicleTrack> getAll() {
		if(this.quantity <= 0) {
			return null;
		}
		List<VehicleTrack> return_ = new ArrayList<VehicleTrack>();
		for (String name: vehicles.keySet()){
            String key = name.toString();
            VehicleTrack value = vehicles.get(name);  
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
