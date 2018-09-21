package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LandkodeEncoderTest {


    @Test
    public void happyPath(){

        LandkodeEncoder encoder = new LandkodeEncoder();
        assertTrue(encoder.encode("ABW").equals("657"));
    }
}