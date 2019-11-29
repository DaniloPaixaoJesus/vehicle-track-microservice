package br.com.danilopaixao.vehicle.track.resource;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackRedis;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;


@RestController
@RequestMapping("/api/v1/vehicle-track")
public class VehicleTrackResource {
	
	@Autowired
	private VehicleTrackService service;
	
	@GetMapping(value="", 
				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Iterable<VehicleTrackRedis> findAll() {
		return service.findAll();
	}
	
	@GetMapping(value = "/queue/{vin}", 
				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public VehicleTrackRedis getVehicleTrack(@PathVariable("vin") final String vin) {
		return service.getVehicleTrack(vin);
	}
	
	@PostMapping(value = "")
	public VehicleTrackRedis insertNewVehicleTrack(@RequestBody(required = true) final VehicleTrackRedis vehicleTrack) {
		return service.insertVehicleTrack(vehicleTrack);
	}
	
	@GetMapping(value = "/near/{latitude}/{longitude}/{distance}")
	public List<VehicleTrack> findNearest(@PathVariable("latitude") final double latitude, 
			@PathVariable("longitude") final double longitude, @PathVariable("distance") final double distance) {
		return service.findNearest(latitude, longitude, distance);
	}
	
	@PutMapping("/{vin}/{lat}/{lon}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<VehicleTrackRedis> updateOnlineStatus(@PathVariable("vin") final String vin,
			@PathVariable("lat") final double lat, @PathVariable("lon") final double lon) throws Throwable {
		double[] position = {lat, lon};
		VehicleTrackRedis vehicleTrack = service.insertIntoQueueOnlineStatus(vin, position);
		
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		if(vehicleTrack == null) {
			return new ResponseEntity<VehicleTrackRedis>(null, null, HttpStatus.NOT_FOUND);
		}
		
		String locationHeader = "/api/v1/vehicle-track/queue/"+vehicleTrack.getVin();
		header.put("Location", Arrays.asList(locationHeader));
		
		return new ResponseEntity<VehicleTrackRedis>(vehicleTrack, header, HttpStatus.ACCEPTED);
	}
	
}
