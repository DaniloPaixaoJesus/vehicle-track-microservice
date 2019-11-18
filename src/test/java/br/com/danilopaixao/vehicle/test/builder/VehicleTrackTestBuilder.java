package br.com.danilopaixao.vehicle.test.builder;

import java.time.ZonedDateTime;

import org.apache.commons.lang.RandomStringUtils;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;

public class VehicleTrackTestBuilder {
	
	private String vin;
	private String queue;
	private StatusEnum status;
	private ZonedDateTime dtStatus;
	private ZonedDateTime dtIniStatus;
	private ZonedDateTime dateTime = ZonedDateTime.now();;
	
	public VehicleTrack build() {
		return new VehicleTrack(vin, queue, status, dtStatus, dtIniStatus);
	}
	
	public VehicleTrack buildRandom(int minBefore, int minIniBefore, StatusEnum status) {
		return new VehicleTrack(RandomStringUtils.random(25, true, true), 
						   RandomStringUtils.random(15, true, false),
						   status,
						   dateTime.plusMinutes(minBefore),
						   dateTime.plusMinutes(minIniBefore)
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

	public VehicleTrackTestBuilder setDtStatus(ZonedDateTime dtStatus) {
		this.dtStatus = dtStatus;
		return this;
	}
	
	public VehicleTrackTestBuilder setDtIniStatus(ZonedDateTime dtIniStatus) {
		this.dtIniStatus = dtIniStatus;
		return this;
	}

	
}
