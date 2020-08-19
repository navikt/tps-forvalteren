package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;

@RunWith(MockitoJUnitRunner.class)
public class SendSkdMeldingTilGitteMiljoerTest {

    private static final String ENV1 = "miljø1";
    private static final String ENV2 = "miljø2";
    private static final String MESSAGE = "melding";

    @Mock
    private SendEnSkdMelding sendEnSkdMelding;

    @InjectMocks
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;

    @Before
    public void setup() {
        skdRequestMeldingDefinition = new TpsSkdRequestMeldingDefinition();
    }

    @Test
    public void setSendToMultipleEnvironments() {

        when(sendEnSkdMelding.sendSkdMelding(anyString(), eq(skdRequestMeldingDefinition), anyString())).thenReturn(MESSAGE);
        sendSkdMeldingTilGitteMiljoer.execute(MESSAGE, skdRequestMeldingDefinition, Set.of(ENV1, ENV2));

        verify(sendEnSkdMelding).sendSkdMelding(eq(MESSAGE), eq(skdRequestMeldingDefinition), eq(ENV1));
        verify(sendEnSkdMelding).sendSkdMelding(eq(MESSAGE), eq(skdRequestMeldingDefinition), eq(ENV2));
    }

    @Test
    public void sendToOneEnvironment() {

        sendSkdMeldingTilGitteMiljoer.execute(MESSAGE, skdRequestMeldingDefinition, ENV1);

        verify(sendEnSkdMelding).sendSkdMelding(eq(MESSAGE), eq(skdRequestMeldingDefinition), eq(ENV1));
    }
}