package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleTrackWSocket implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7774185072098722239L;
	
	private String vin;
	private String status;
	private Location geolocation;

	public VehicleTrackWSocket() {
	}
	
	public VehicleTrackWSocket(String vin, String status, Location geolocation) {
		super();
		this.setVin(vin);
		this.setStatus(status);
		this.setGeolocation(geolocation);
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Location getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(Location geolocation) {
		this.geolocation = geolocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geolocation == null) ? 0 : geolocation.hashCode());
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
		VehicleTrackWSocket other = (VehicleTrackWSocket) obj;
		if (geolocation == null) {
			if (other.geolocation != null)
				return false;
		} else if (!geolocation.equals(other.geolocation))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
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
		return "VehicleTrackWSocket [vin=" + vin + ", status=" + status + ", geolocation=" + geolocation + "]";
	}

	

}
