package no.nav.tps.forvalteren.service.command.testdata.skd.impl;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.Familieendring;
import no.nav.tps.forvalteren.service.command.testdata.FinnBarnTilForeldreFraRelasjoner;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdFelterContainerTrans2;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdOpprettSkdMeldingMedHeaderOgInnhold;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies.BarnetranseSkdParameterStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSkdCreateFamilierelasjonerTest {

    @InjectMocks
    private DefaultSkdCreateFamilierelasjoner defaultSkdCreateFamilierelasjoner;

    @Value("${environment.class}")
    private String deployedEnvironment;

    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Mock
    private BarnetranseSkdParameterStrategy barnetranseSkdParameterStrategy;

    @Mock
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Mock
    private SkdFelterContainerTrans2 skdFelterContainerTrans2;

    @Mock
    private Familieendring familieendring;

    @Mock
    private FinnBarnTilForeldreFraRelasjoner finnBarnTilForeldreFraRelasjoner;

    private Person foreldre = aMalePerson().build();
    private List<Person> barn = new ArrayList<>();
    private List<Relasjon> foreldreBarnRelasjoner = new ArrayList<>();
    private List<String> environments = new ArrayList<>();
    private Map<String, String> skdParams = new HashMap<>();
    private String skdMelding = "SKDMELDING";
    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition = null;

    @Before
    public void setup() {
        when(familieendring.resolve()).thenReturn(skdRequestMeldingDefinition);
        when(barnetranseSkdParameterStrategy.execute(foreldre, barn)).thenReturn(skdParams);
        when(skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParams, skdFelterContainerTrans2)).thenReturn(skdMelding);
        when(finnBarnTilForeldreFraRelasjoner.execute(foreldreBarnRelasjoner)).thenReturn(barn);
    }

    @Test
    public void verifyMethodCalls() {
        defaultSkdCreateFamilierelasjoner.execute(foreldre, foreldreBarnRelasjoner, environments);

        verify(finnBarnTilForeldreFraRelasjoner).execute(foreldreBarnRelasjoner);
        verify(barnetranseSkdParameterStrategy).execute(foreldre, barn);
        verify(skdOpprettSkdMeldingMedHeaderOgInnhold).execute(skdParams, skdFelterContainerTrans2);
        verify(sendSkdMeldingTilGitteMiljoer).execute(skdMelding, skdRequestMeldingDefinition, new HashSet<>(environments));
    }
}