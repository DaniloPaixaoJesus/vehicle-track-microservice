package br.com.danilopaixao.vehicle.test.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.danilopaixao.vehicle.test.builder.VehicleTrackTestBuilder;
import br.com.danilopaixao.vehicle.track.model.VehicleTrack;
import br.com.danilopaixao.vehicle.track.validator.Validator;
import br.com.danilopaixao.vehicle.track.validator.VehicleTrackValidator;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTrackValidatorTest {

	Validator<VehicleTrack> validator = new VehicleTrackValidator();
	
	@Test
	public void testValidatorTimeLimiteNOk() throws Exception {
		Whitebox.setInternalState(validator, "timeLimiteMinutes", 200L);
        assertFalse(validator.validar(new VehicleTrackTestBuilder()
        									.setDtStatus(ZonedDateTime.now())
        									.setDtIniStatus(ZonedDateTime.now().plusMinutes(-300))
        									.build()));
	}
	
	@Test
	public void testValidatorTimeLimiteOk() throws Exception {
		Whitebox.setInternalState(validator, "timeLimiteMinutes", 200L);
        assertTrue(validator.validar(new VehicleTrackTestBuilder()
        									.setDtStatus(ZonedDateTime.now())
        									.setDtIniStatus(ZonedDateTime.now().plusMinutes(-100))
        									.build()));
	}
	
}
