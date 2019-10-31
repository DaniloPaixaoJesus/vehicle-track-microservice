package br.com.danilopaixao.vehicle.test.builder;

import org.apache.commons.lang.RandomStringUtils;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Vehicle;

public class VehicleTestBuilder {
	
	private String vin;
	private String regNumber;
	private String name;
	private String driverId;
	private StatusEnum status;
	
	public Vehicle buildRandom(StatusEnum status) {
		return new Vehicle(RandomStringUtils.random(25, true, true), 
						   RandomStringUtils.random(15, true, true),
						   RandomStringUtils.random(10, true, false), 
						   status,
						   RandomStringUtils.random(15, true, true));
	}
	
	public Vehicle build() {
		return new Vehicle(vin, regNumber, name, status, driverId);
	}
	
	public VehicleTestBuilder setVin(String vin) {
		this.vin = vin;
		return this;
	}
	public VehicleTestBuilder setRegNumber(String regNumber) {
		this.regNumber = regNumber;
		return this;
	}
	public VehicleTestBuilder setName(String name) {
		this.name = name;
		return this;
	}
	public VehicleTestBuilder setDriverId(String driverId) {
		this.driverId = driverId;
		return this;
	}
	public VehicleTestBuilder setStatus(StatusEnum status) {
		this.status = status;
		return this;
	}
	
	

}
