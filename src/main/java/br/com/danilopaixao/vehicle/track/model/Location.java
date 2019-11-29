package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;

public class Location implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5880311321896204946L;

	private double latitude;
	
	private double longitude;
	
	
	//private String type = LocationTypeEnum.POINT.getType();
	
	public Location() {}

	public Location(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	
	
	
}
