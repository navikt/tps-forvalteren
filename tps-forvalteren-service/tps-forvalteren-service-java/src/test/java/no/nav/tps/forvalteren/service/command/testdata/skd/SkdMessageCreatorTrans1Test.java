package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02;
import no.nav.tps.forvalteren.domain.test.provider.PersonProvider;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;

@RunWith(MockitoJUnitRunner.class)
public class SkdMessageCreatorTrans1Test {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Mock
    private SkdParametersCreatorService skdParametersCreatorService;

    @Mock
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Mock
    private SkdFelterContainerTrans1 skdFelterContainer;

    @Mock
    private GetSkdMeldingByName getSkdMeldingByName;

    @Mock
    private GenerateSkdMelding generateSkdMelding;

    private static final boolean ADD_HEADER = true;
    private final String VIGSEL = "Vigsel";
    private final String INNVANDRING = "Innvandring";
    private final String SKDMELDING = "SKDMELDING";

    private Person person = PersonProvider.aMalePerson().build();
    private Person person2 = PersonProvider.aMalePerson().build();
    private Person person3 = PersonProvider.aMalePerson().build();
    private List<Person> persons = new ArrayList<>();

    @Test
    public void illegalSkdMeldingByNameThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        when(getSkdMeldingByName.execute(anyString())).thenReturn(Optional.empty());
        
        skdMessageCreatorTrans1.execute(VIGSEL, persons, ADD_HEADER);
    }

    @Test
    public void checkThatGenerateSkdMeldingGetsCalledMultipleTimes() {
        persons.add(person);
        persons.add(person2);
        persons.add(person3);
        Map<String, String> skdParametere = new HashMap<>();

        TpsSkdRequestMeldingDefinition tpsSkdRequestMeldingDefinition = new InnvandringAarsakskode02().resolve();
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = Optional.of(tpsSkdRequestMeldingDefinition);
        
        when(getSkdMeldingByName.execute(INNVANDRING)).thenReturn(skdRequestMeldingDefinitionOptional);
        when(skdParametersCreatorService.execute(any(TpsSkdRequestMeldingDefinition.class), any(Person.class))).thenReturn(new SkdMeldingTrans1());
//        when(skdOpprettSkdMeldingMedHeaderOgInnhold.sendMessage(skdParametere, skdFelterContainer, ADD_HEADER)).thenReturn(SKDMELDING);

        skdMessageCreatorTrans1.execute(INNVANDRING, persons, ADD_HEADER);

        verify(generateSkdMelding).execute( skdRequestMeldingDefinitionOptional.get(), person, ADD_HEADER);
        verify(generateSkdMelding).execute( skdRequestMeldingDefinitionOptional.get(), person2, ADD_HEADER);
        verify(generateSkdMelding).execute( skdRequestMeldingDefinitionOptional.get(), person3, ADD_HEADER);
    }

}