package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;


public class VehicleTrackWSocket implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8659998311539411802L;
	
	private String vin;
	private StatusEnum status;

	public VehicleTrackWSocket() {
	}
	
	public VehicleTrackWSocket(String vin, StatusEnum status) {
		super();
		this.setVin(vin);
		this.setStatus(status);
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	
	
	
	
}
