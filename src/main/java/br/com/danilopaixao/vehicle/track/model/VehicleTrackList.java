package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;
import java.util.List;

public class VehicleTrackList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3182987159693795275L;
	
	public VehicleTrackList() {}
	
	public VehicleTrackList(List<VehicleTrack> vehicleTrackList) {
		super();
		this.vehicleTrackList = vehicleTrackList;
	}
	
	private List<VehicleTrack> vehicleTrackList;

	public List<VehicleTrack> getVehicleTrackList() {
		return vehicleTrackList;
	}

	public void setVehicleTrackList(List<VehicleTrack> vehicleTrackList) {
		this.vehicleTrackList = vehicleTrackList;
	}

}
