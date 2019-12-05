package br.com.danilopaixao.vehicle.track.validator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;
import br.com.danilopaixao.vehicle.track.utils.DateTimeUtils;

public class VehicleTrackValidator implements Validator<VehicleTrackCache>{
	
	private Logger logger = LoggerFactory.getLogger(VehicleTrackValidator.class);
	
	@Value("${br.com.danilopaixao.validator.timelimiteminutes}")
	private long timeLimiteMinutes;
	
	@PostConstruct
    public void init() {
        //
    }
	
	@Override
	public boolean validar(VehicleTrackCache vehicleTrack) {
//		logger.info("##VehicleValidator#timeLimit: {}", timeLimiteMinutes);
//		long diff = DateTimeUtils.minsDiff(vehicleTrack.getDtIniStatus(), vehicleTrack.getDtStatus());
//		return diff > timeLimiteMinutes ? Boolean.FALSE : Boolean.TRUE;
		return Boolean.TRUE;
	}

	@PreDestroy
    public void destroy() {
        //
    }

}
