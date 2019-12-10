package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleTrackList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3182987159693795275L;
	
	public VehicleTrackList() {}
	
	public VehicleTrackList(List<VehicleTrackCache> vehicleTrackList) {
		super();
		this.vehicleTrackList = vehicleTrackList;
	}
	
	private List<VehicleTrackCache> vehicleTrackList;

	public List<VehicleTrackCache> getVehicleTrackList() {
		return vehicleTrackList;
	}

	public void setVehicleTrackList(List<VehicleTrackCache> vehicleTrackList) {
		this.vehicleTrackList = vehicleTrackList;
	}

}
