package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class HusbokstavEncoderTest {

    @Test
    public void happyPath(){

        HusbokstavEncoder encoder = new HusbokstavEncoder();
        assertTrue(encoder.encode("A").equals("9901"));
    }
}