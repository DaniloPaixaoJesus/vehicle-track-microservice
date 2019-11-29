package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;
import java.util.List;

public class VehicleTrackList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3182987159693795275L;
	
	public VehicleTrackList() {}
	
	public VehicleTrackList(List<VehicleTrackRedis> vehicleTrackList) {
		super();
		this.vehicleTrackList = vehicleTrackList;
	}
	
	private List<VehicleTrackRedis> vehicleTrackList;

	public List<VehicleTrackRedis> getVehicleTrackList() {
		return vehicleTrackList;
	}

	public void setVehicleTrackList(List<VehicleTrackRedis> vehicleTrackList) {
		this.vehicleTrackList = vehicleTrackList;
	}

}
