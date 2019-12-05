package br.com.danilopaixao.vehicle.track.mapper;

import java.util.function.Function;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;

public final class VehicleTrackMapper {
	
	public static final Function<VehicleTrackCache, VehicleTrack> 
		fromVehicleTrackCache = vehicle -> new VehicleTrack(vehicle.getVin(), 
												vehicle.getQueue(), 
												vehicle.getStatus(), 
												vehicle.getDtStatus(), 
												vehicle.getGeolocation());
		
		public static final VehicleTrack fromVehicleTrackCache(VehicleTrackCache vehicle) {
			return new VehicleTrack(vehicle.getVin(), 
					vehicle.getQueue(), 
					vehicle.getStatus(), 
					vehicle.getDtStatus(), 
					vehicle.getGeolocation());
		}

}
