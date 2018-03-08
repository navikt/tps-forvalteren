package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StatsborgerskapEncoderTest {


    @Test
    public void happyPath(){

        StatsborgerskapEncoder encoder = new StatsborgerskapEncoder();
        assertTrue(encoder.encode("ABW").equals("657"));
    }
}