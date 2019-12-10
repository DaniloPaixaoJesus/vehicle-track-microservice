package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4626925597094078277L;

	private List<Vehicle> vehicleList;
	
	public VehicleList() {}
	
	public VehicleList(List<Vehicle> vehicleList) {
		super();
		this.setVehicleList(vehicleList);
	}

	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}


}
