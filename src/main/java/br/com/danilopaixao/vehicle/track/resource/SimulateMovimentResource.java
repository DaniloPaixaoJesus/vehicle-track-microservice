package br.com.danilopaixao.vehicle.track.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.danilopaixao.vehicle.track.service.SimulateMovimentService;


@RestController
@RequestMapping("/api/v1/vehicle-track/moviment")
public class SimulateMovimentResource {
	
	@GetMapping(value="/ON", 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void on() {
		SimulateMovimentService.STATUS_RUNNER = "ON";
	}
	
	@GetMapping(value="/OFF", 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void off() {
		SimulateMovimentService.STATUS_RUNNER = "OFF";
	}
	
	
}
