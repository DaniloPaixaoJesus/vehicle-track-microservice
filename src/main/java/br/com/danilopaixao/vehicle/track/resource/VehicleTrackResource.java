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
import org.springframework.web.server.ResponseStatusException;

import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;
import br.com.danilopaixao.vehicle.track.service.VehicleTrackService;


@RestController
@RequestMapping("/api/v1/vehicle-track")
public class VehicleTrackResource {
	
	@Autowired
	private VehicleTrackService service;
	
	@PostMapping(value="/restore-redis", 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Iterable<VehicleTrackCache> restoreRedis() {
		service.loadRedisDataFromMongo();
		return service.findAllVehicleTrackCache();
	}
	
	@GetMapping(value="", 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<VehicleTrack> findAllVehiclesTrack() {
		return service.findAllVehicleTrack();
	}
	
	@GetMapping(value="/{vin}", 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<VehicleTrack> findVehiclesTrackByVin(@PathVariable("vin") final String vin) {
		List<VehicleTrack> r = service.findVehicleTrackByVin(vin);
		if(r == null 
				|| r.isEmpty()) {
			throw new ResponseStatusException(
			          HttpStatus.NOT_FOUND, "vin not found");
		}
		return r;
	}
	
	@GetMapping(value="/cache", 
				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Iterable<VehicleTrackCache> findAllVehiclesTrackCache() {
		return service.findAllVehicleTrackCache();
	}
	
	@GetMapping(value="/cache/{vin}", 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public VehicleTrackCache findVehiclesTrackCacheByVin(@PathVariable("vin") final String vin) {
		VehicleTrackCache r = service.getVehicleTrackCache(vin);
		if(r == null ) {
			throw new ResponseStatusException(
			          HttpStatus.NOT_FOUND, "vin not found");
		}
		return r;
	}
	
	@PostMapping(value = "/cache",
				 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public VehicleTrackCache insertNewVehicleTrack(@RequestBody(required = true) final VehicleTrackCache vehicleTrack) {
		return service.insertVehicleTrackCache(vehicleTrack);
	}
	
	@GetMapping(value = "/queue/{vin}", 
				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public VehicleTrackCache getVehicleTrack(@PathVariable("vin") final String vin) {
		VehicleTrackCache r = service.getVehicleTrackCache(vin);
		if(r == null ) {
			throw new ResponseStatusException(
			          HttpStatus.NOT_FOUND, "vin not found");
		}
		return r;
	}
	
	
	@GetMapping(value = "/near/{latitude}/{longitude}/{distance}")
	public List<VehicleTrack> findNearest(@PathVariable("latitude") final double latitude, 
			@PathVariable("longitude") final double longitude, @PathVariable("distance") final double distance) {
		return service.findNearest(latitude, longitude, distance);
	}
	
	@GetMapping(value = "/near2/{latitude}/{longitude}/{distance}")
	public List<VehicleTrack> findNearest2(@PathVariable("latitude") final double latitude, 
			@PathVariable("longitude") final double longitude, @PathVariable("distance") final double distance) {
		return service.findNearest2(latitude, longitude, distance);
	}
	
	@PutMapping("/{vin}/{lat}/{lon}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<VehicleTrackCache> updateOnlineStatus(@PathVariable("vin") final String vin,
			@PathVariable("lat") final double lat, @PathVariable("lon") final double lon) throws Throwable {
		double[] position = {lat, lon};
		VehicleTrackCache vehicleTrack = service.insertIntoQueueOnlineStatus(vin, position);
		
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		if(vehicleTrack == null) {
			return new ResponseEntity<VehicleTrackCache>(null, null, HttpStatus.NOT_FOUND);
		}
		
		String locationHeader = "/api/v1/vehicle-track/queue/"+vehicleTrack.getVin();
		header.put("Location", Arrays.asList(locationHeader));
		
		return new ResponseEntity<VehicleTrackCache>(vehicleTrack, header, HttpStatus.ACCEPTED);
	}
	
}
