package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1564541072875906508L;
	
	private String vin;
	private String regNumber;
	private String name;
	private String driverId;
	private StatusEnum status;
	private VehicleTrack vehicleTrack;

	public Vehicle() {}
	
	public Vehicle(String vin, String regNumber, String name, StatusEnum status, 
						String driverId, VehicleTrack vehicleTrack) {
		super();
		this.vin = vin;
		this.regNumber = regNumber;
		this.name = name;
		this.status = status;
		this.driverId = driverId;
		this.vehicleTrack = vehicleTrack;
	}

	public Vehicle(String vin, String regNumber, String name, StatusEnum status, String driverId) {
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

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	
	public VehicleTrack getVehicleTrack() {
		return vehicleTrack;
	}

	public void setVehicleTrack(VehicleTrack vehicleTrack) {
		this.vehicleTrack = vehicleTrack;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((regNumber == null) ? 0 : regNumber.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((vin == null) ? 0 : vin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		if (driverId == null) {
			if (other.driverId != null)
				return false;
		} else if (!driverId.equals(other.driverId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (regNumber == null) {
			if (other.regNumber != null)
				return false;
		} else if (!regNumber.equals(other.regNumber))
			return false;
		if (status != other.status)
			return false;
		if (vin == null) {
			if (other.vin != null)
				return false;
		} else if (!vin.equals(other.vin))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vehicle [vin=" + vin + ", regNumber=" + regNumber + ", name=" + name + ", driverId=" + driverId
				+ ", status=" + status + "]";
	}

}
