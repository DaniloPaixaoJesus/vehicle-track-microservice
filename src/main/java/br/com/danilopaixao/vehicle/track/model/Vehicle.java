package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.danilopaixao.vehicle.track.config.LocalDateTimeDeserializer;
import br.com.danilopaixao.vehicle.track.config.LocalDateTimeSerializer;
import br.com.danilopaixao.vehicle.track.enums.StatusEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5805672437440193950L;
	
	@Id
	private String vin;
	private String regNumber;
	private String name;
	private StatusEnum status;
	private Location geolocation;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime dtStatus;

	public Vehicle() {}

	public Vehicle(String vin, String regNumber, String name, StatusEnum status) {
		super();
		this.vin = vin;
		this.regNumber = regNumber;
		this.name = name;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Location getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(Location geolocation) {
		this.geolocation = geolocation;
	}
	
	public LocalDateTime getDtStatus() {
		return dtStatus;
	}

	public void setDtStatus(LocalDateTime dtStatus) {
		this.dtStatus = dtStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		return "Vehicle [vin=" + vin + ", regNumber=" + regNumber + ", name=" + name + ", status=" + status + "]";
	}

}
