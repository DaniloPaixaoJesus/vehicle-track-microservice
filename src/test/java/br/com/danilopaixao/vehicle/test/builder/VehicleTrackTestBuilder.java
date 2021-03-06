package br.com.danilopaixao.vehicle.test.builder;

import java.time.LocalDateTime;

import org.apache.commons.lang.RandomStringUtils;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.Location;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;

public class VehicleTrackTestBuilder {
	
	private String vin;
	private String queue;
	private StatusEnum status;
	private LocalDateTime dtStatus;
	private LocalDateTime dateTime = LocalDateTime.now();;
	
	public VehicleTrackCache build() {
		return new VehicleTrackCache(vin, queue, status, dtStatus, new Location());
	}
	
	public VehicleTrackCache buildRandom(int minBefore, int minIniBefore, StatusEnum status) {
		return new VehicleTrackCache(RandomStringUtils.random(25, true, true), 
						   RandomStringUtils.random(15, true, false),
						   status,
						   dateTime.plusMinutes(minBefore),
						   new Location()
						   );
	}
	
	public VehicleTrackTestBuilder setVin(String vin) {
		this.vin = vin;
		return this;
	}

	public VehicleTrackTestBuilder setQueue(String queue) {
		this.queue = queue;
		return this;
	}

	public VehicleTrackTestBuilder setStatus(StatusEnum status) {
		this.status = status;
		return this;
	}

	public VehicleTrackTestBuilder setDtStatus(LocalDateTime dtStatus) {
		this.dtStatus = dtStatus;
		return this;
	}
	
}
