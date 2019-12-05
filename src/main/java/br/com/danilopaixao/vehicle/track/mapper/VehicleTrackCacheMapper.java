package br.com.danilopaixao.vehicle.track.mapper;

import java.util.function.Function;

import br.com.danilopaixao.vehicle.track.model.Vehicle;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;

public final class VehicleTrackCacheMapper {
	
	public static final Function<Vehicle, VehicleTrackCache> 
		fromVehicle = vehicle -> new VehicleTrackCache(vehicle.getVin(), 
												null, 
												vehicle.getStatus(), 
												null, 
												null);

}
