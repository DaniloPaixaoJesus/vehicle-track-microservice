package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("VehicleTrack")
public class VehicleTrack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1569461794138116014L;

	@Id
	private String vin;
	private String queue;
	private String status;
	private Date dtStatus;

	public VehicleTrack() {
	}

	public VehicleTrack(String vin, String queue, String status, Date dtStatus) {
		super();
		this.vin = vin;
		this.queue = queue;
		this.status = status;
		this.dtStatus = dtStatus;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}


	public void setName(String status) {
		this.setStatus(status);
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDtStatus() {
		return dtStatus;
	}

	public void setDtStatus(Date dtStatus) {
		this.dtStatus = dtStatus;
	}

}
