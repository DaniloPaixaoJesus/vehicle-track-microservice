package br.com.danilopaixao.vehicle.track.resource;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;


@RestController
@RequestMapping("/api/v1/vehicle-track")
public class VehicleTrackResource {
	
	@Autowired
	private VehicleTrackService service;
	
	@RequestMapping(value = "/{queue}/queue", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public VehicleTrack getVehicleTrack(@PathVariable("queue") final String queue) {
		return service.getQueue(queue);
	}
	
	
	@PutMapping("/{vin}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<String> updateStatus(@PathVariable("vin") final String vin) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		String queueLocation = service.insertIntoQueue(vin);
		header.put("Location", Arrays.asList(queueLocation));
		return new ResponseEntity<String>(queueLocation, header, HttpStatus.ACCEPTED);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
