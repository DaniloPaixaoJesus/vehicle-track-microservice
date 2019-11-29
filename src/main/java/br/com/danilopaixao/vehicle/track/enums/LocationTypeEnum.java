package br.com.danilopaixao.vehicle.track.enums;

public enum LocationTypeEnum {

	POINT("Point");
	String type;
	
	LocationTypeEnum(String type){
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
}
