package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.utils.DateTimeUtils;

@RedisHash("VehicleTrack")
public class VehicleTrack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1569461794138116014L;

	@Id
	private String vin;
	private String queue;
	private StatusEnum status;
	@JsonIgnore
	private ZonedDateTime dtStatus;
	@JsonIgnore
	private ZonedDateTime dtIniStatus;

	public VehicleTrack() {
	}

	public VehicleTrack(String vin, String queue, StatusEnum status, ZonedDateTime dtStatus, ZonedDateTime dtIniStatus) {
		super();
		this.vin = vin;
		this.queue = queue;
		this.status = status;
		this.dtStatus = dtStatus;
		this.dtIniStatus = dtIniStatus;
	}
	
	public boolean isOffLineVehicle(int seconds) {
		long diff = DateTimeUtils.secsDiff(dtStatus, ZonedDateTime.now());
		return diff > seconds ? Boolean.TRUE : Boolean.FALSE;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public ZonedDateTime getDtStatus() {
		return dtStatus;
	}

	public void setDtStatus(ZonedDateTime dtStatus) {
		this.dtStatus = dtStatus;
	}

	public ZonedDateTime getDtIniStatus() {
		return dtIniStatus;
	}

	public void setDtIniStatus(ZonedDateTime dtIniStatus) {
		this.dtIniStatus = dtIniStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtStatus == null) ? 0 : dtStatus.hashCode());
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
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
		VehicleTrack other = (VehicleTrack) obj;
		if (dtStatus == null) {
			if (other.dtStatus != null)
				return false;
		} else if (!dtStatus.equals(other.dtStatus))
			return false;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
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
		return "VehicleTrack [vin=" + vin + ", queue=" + queue + ", status=" + status + ", dtStatus=" + dtStatus + "]";
	}
	
	


}
