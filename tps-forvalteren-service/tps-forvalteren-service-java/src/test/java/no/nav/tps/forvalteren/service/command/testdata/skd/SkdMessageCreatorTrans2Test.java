package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.BarnetranseSkdParameterStrategy;

@RunWith(MockitoJUnitRunner.class)
public class SkdMessageCreatorTrans2Test {

    @Mock
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Mock
    private GetSkdMeldingByName getSkdMeldingByName;

    @Mock
    private SkdFelterContainerTrans2 skdFelterContainer;

    @Mock
    private BarnetranseSkdParameterStrategy barnetranseSkdParameterStrategy;

    @InjectMocks
    private SkdMessageCreatorTrans2 skdMessageCreatorTrans2;

    @Mock
    private Map<String, String> skdParametere;
    
    @Mock
    private Person forelder;
    
    @Mock
    private List<Person> barn;

    private String skdMeldingNavn = "Innvandring";
    private boolean addHeader = true;
    private String skdMelding = "skdmelding";

    @Before
    public void setup() {
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = Optional.of(new TpsSkdRequestMeldingDefinition());
        when(getSkdMeldingByName.execute(skdMeldingNavn)).thenReturn(skdRequestMeldingDefinitionOptional);
        when(barnetranseSkdParameterStrategy.execute(forelder, barn)).thenReturn(skdParametere);
        when(skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere, skdFelterContainer, addHeader)).thenReturn(skdMelding);
    }

    @Test
    public void tester() {
        List<SkdMeldingTrans2> result = skdMessageCreatorTrans2.execute(skdMeldingNavn, forelder, barn, addHeader);
        
        verify(getSkdMeldingByName).execute(skdMeldingNavn);
        verify(barnetranseSkdParameterStrategy).execute(forelder, barn);
        verify(skdOpprettSkdMeldingMedHeaderOgInnhold).execute(skdParametere, skdFelterContainer, addHeader);
        
        assertThat(result, hasSize(1));
        assertThat(result.get(0).toString(), is(skdMelding));
        
    }

}