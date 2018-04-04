package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;

@RunWith(MockitoJUnitRunner.class)
public class GenerateSkdMeldingTest { //FIXME denne testen har null verdi. Gi den verdi ved å teste generering av skdmelding for de ulike skdMeldingstyper. Mock ut repository. ellers skal alt fungere.

    @Mock
    private SkdParametersCreatorService skdParametersCreatorService;

    @InjectMocks
    private GenerateSkdMelding generateSkdMelding;

    @Mock
    private SkdFelterContainer skdFelterContainer;

    @Mock
    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;

    @Mock
    private Person person;

    private boolean addHeader;

    private static final String SKD_MELDING = "SKDMELDING";

    @Before
    public void setup() {
        when(skdParametersCreatorService.execute(skdRequestMeldingDefinition, person)).thenReturn(new SkdMeldingTrans1());
    }

    @Test
    @Ignore("under arbeid - SkdmeldingTrans1 .toString() må implementeres først")
    public void verifyServiceCall() {
        String skdMelding = generateSkdMelding.execute(skdFelterContainer, skdRequestMeldingDefinition, person, addHeader);

        verify(skdParametersCreatorService).execute(skdRequestMeldingDefinition, person);

        assertThat(skdMelding, is(SKD_MELDING));
    }

}