package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;

public class VehicleTrack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1569461794138116014L;

	private String queue;
	private String vin;
	private String status;

	public VehicleTrack() {
	}

	public VehicleTrack(String vin, String queue, String status) {
		super();
		this.vin = vin;
		this.queue = queue;
		this.status = status;
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

	public void setName(String status) {
		this.status = status;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

}
