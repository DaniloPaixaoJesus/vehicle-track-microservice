package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;

public class Vehicle implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1564541072875906508L;
	
	private String vin;
	private String regNumber;
	private String name;
	private String driverId;
	private String status;

	public Vehicle() {}

	public Vehicle(String vin, String regNumber, String name, String status, String driverId) {
		super();
		this.vin = vin;
		this.regNumber = regNumber;
		this.name = name;
		this.status = status;
		this.driverId = driverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
