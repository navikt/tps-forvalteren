package no.nav.tps.forvalteren.service.command.testdata.skd;

import static java.util.Arrays.asList;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;

@RunWith(MockitoJUnitRunner.class)
public class SendSkdMeldingTilGitteMiljoerTest {

    private static final String ENV1 = "miljø";
    private static final String ENV2 = "miljø";
    private static final String MESSAGE = "melding";

    @Mock
    private SendEnSkdMelding sendEnSkdMelding;

    @Mock
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @InjectMocks
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;

    @Before
    public void setup() {
        skdRequestMeldingDefinition = new TpsSkdRequestMeldingDefinition();
        when(filterEnvironmentsOnDeployedEnvironment.execute(anySet())).thenReturn(newHashSet(asList(ENV1)));
    }

    @Test
    public void setSendToMultipleEnvironments() {

        sendSkdMeldingTilGitteMiljoer.execute(MESSAGE, skdRequestMeldingDefinition, newHashSet(asList(ENV1, ENV2)));

        verify(sendEnSkdMelding).sendSkdMelding(eq(MESSAGE), eq(skdRequestMeldingDefinition), eq(ENV1));
    }

    @Test
    public void sendToOneEnvironment() {

        sendSkdMeldingTilGitteMiljoer.execute(MESSAGE, skdRequestMeldingDefinition, ENV1);

        verify(sendEnSkdMelding).sendSkdMelding(eq(MESSAGE), eq(skdRequestMeldingDefinition), eq(ENV1));
    }
}