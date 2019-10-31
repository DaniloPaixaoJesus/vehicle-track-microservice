package br.com.danilopaixao.vehicle.test.builder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;

public class VehicleTrackTestBuilder {
	
	private String vin;
	private String queue;
	private StatusEnum status;
	private Date dtStatus;
	private LocalDateTime dateTime = LocalDateTime.now();;
	
	public VehicleTrack buildRandom(int minBefore, StatusEnum status) {
		return new VehicleTrack(RandomStringUtils.random(25, true, true), 
						   RandomStringUtils.random(15, true, false),
						   status,
						   Date.from(dateTime.plusMinutes(minBefore).atZone(ZoneId.systemDefault()).toInstant()));
	}
	
	public void setVin(String vin) {
		this.vin = vin;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public void setDtStatus(Date dtStatus) {
		this.dtStatus = dtStatus;
	}

	
}
