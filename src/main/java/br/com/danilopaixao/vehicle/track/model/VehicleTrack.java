package br.com.danilopaixao.vehicle.track.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.danilopaixao.vehicle.track.config.LocalDateTimeDeserializer;
import br.com.danilopaixao.vehicle.track.config.LocalDateTimeSerializer;
import br.com.danilopaixao.vehicle.track.enums.StatusEnum;
import br.com.danilopaixao.vehicle.track.utils.DateTimeUtils;

//@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document("vehicleTrack")
public class VehicleTrack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1569461794138116014L;

	@Id
	private String id;
	
	private String vin;
	private String queue;
	private StatusEnum status;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime dtStatus;
	//db.VehicleTrack.ensureIndex( { location: "2d" } );
	private Location geolocation;

	public VehicleTrack() {
	}

	public VehicleTrack(String vin, String queue, StatusEnum status, LocalDateTime dtStatus, Location geolocation) {
		super();
		this.vin = vin;
		this.queue = queue;
		this.status = status;
		this.dtStatus = dtStatus;
		this.geolocation = geolocation;
	}
	
	public boolean isOffLineVehicle(int seconds) {
		long diff = DateTimeUtils.secsDiff(dtStatus, LocalDateTime.now());
		return diff > seconds ? Boolean.TRUE : Boolean.FALSE;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public LocalDateTime getDtStatus() {
		return dtStatus;
	}

	public void setDtStatus(LocalDateTime dtStatus) {
		this.dtStatus = dtStatus;
	}
	
	public Location getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(Location location) {
		this.geolocation = location;
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
