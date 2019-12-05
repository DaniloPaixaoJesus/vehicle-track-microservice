package br.com.danilopaixao.vehicle.test.service;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.danilopaixao.vehicle.test.builder.VehicleTrackTestBuilder;
import br.com.danilopaixao.vehicle.track.model.VehicleTrackCache;
import br.com.danilopaixao.vehicle.track.validator.Validator;
import br.com.danilopaixao.vehicle.track.validator.VehicleTrackValidator;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTrackValidatorTest {

	Validator<VehicleTrackCache> validator = new VehicleTrackValidator();
//	
//	@Test
//	public void testValidatorTimeLimiteNOk() throws Exception {
//		Whitebox.setInternalState(validator, "timeLimiteMinutes", 200L);
//        assertFalse(validator.validar(new VehicleTrackTestBuilder()
//        									.setDtStatus(LocalDateTime.now())
//        									.build()));
//	}
	
	@Test
	public void testValidatorTimeLimiteOk() throws Exception {
		Whitebox.setInternalState(validator, "timeLimiteMinutes", 200L);
        assertTrue(validator.validar(new VehicleTrackTestBuilder()
        									.setDtStatus(LocalDateTime.now())
        									.build()));
	}
	
}
