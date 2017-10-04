package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.Familieendring;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02;
import no.nav.tps.forvalteren.domain.test.provider.PersonProvider;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;

@RunWith(MockitoJUnitRunner.class)
public class SkdCreatePersonerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private SkdCreatePersoner skdCreatePersoner;

    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Mock
    private SkdParametersCreatorService skdParametersCreatorService;

    @Mock
    private SkdOpprettSkdMeldingMedHeaderOgInnhold skdOpprettSkdMeldingMedHeaderOgInnhold;

    @Mock
    private GetSkdMeldingByName getSkdMeldingByName;

    private final String VIGSEL = "Vigsel";
    private final String FAMILIEENDRING = "Familieendring";
    private final String INNVANDRING = "Innvandring";
    private final String SKDMELDING = "SKDMELDING";

    private List<String> environments = new ArrayList<>(Arrays.asList("u2", "u6"));
    private SkdFelterContainer skdFelterContainer = new SkdFelterContainerTrans1();
    private Person person = PersonProvider.aMalePerson().build();
    private Person person2 = PersonProvider.aMalePerson().build();
    private Person person3 = PersonProvider.aMalePerson().build();
    private List<Person > persons = new ArrayList<>();

    @Before
    public void setup() {

    }

    @Test
    public void illegalSkdMeldingByNameThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        when(getSkdMeldingByName.execute(anyString())).thenReturn(Optional.empty());

        skdCreatePersoner.execute(VIGSEL, persons, environments, skdFelterContainer);
    }

    @Test
    public void createsFamilieendring() {
        persons.add(person);
        TpsSkdRequestMeldingDefinition tpsSkdRequestMeldingDefinition = new Familieendring().resolve();
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = Optional.of(tpsSkdRequestMeldingDefinition);
        Map<String, String> skdParametere = new HashMap<>();

        when(getSkdMeldingByName.execute(FAMILIEENDRING)).thenReturn(skdRequestMeldingDefinitionOptional);
        when(skdParametersCreatorService.execute(skdRequestMeldingDefinitionOptional.get(), person)).thenReturn(skdParametere);
        when(skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere, skdFelterContainer)).thenReturn(SKDMELDING);

        skdCreatePersoner.execute(FAMILIEENDRING, persons, environments, skdFelterContainer);

        verify(sendSkdMeldingTilGitteMiljoer).execute(SKDMELDING, skdRequestMeldingDefinitionOptional.get(), new HashSet<>(environments));
    }

    @Test
    public void checkThatInnvandringSkdMeldingGetsSentMultipleTimes() {
        persons.add(person);
        persons.add(person2);
        persons.add(person3);
        Map<String, String> skdParametere = new HashMap<>();

        TpsSkdRequestMeldingDefinition tpsSkdRequestMeldingDefinition = new InnvandringAarsakskode02().resolve();
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = Optional.of(tpsSkdRequestMeldingDefinition);

        when(getSkdMeldingByName.execute(INNVANDRING)).thenReturn(skdRequestMeldingDefinitionOptional);
        when(skdParametersCreatorService.execute(any(TpsSkdRequestMeldingDefinition.class), any(Person.class))).thenReturn(skdParametere);
        when(skdOpprettSkdMeldingMedHeaderOgInnhold.execute(skdParametere, skdFelterContainer)).thenReturn(SKDMELDING);

        skdCreatePersoner.execute(INNVANDRING, persons, environments, skdFelterContainer);

        verify(sendSkdMeldingTilGitteMiljoer, times(3)).execute(SKDMELDING, skdRequestMeldingDefinitionOptional.get(), new HashSet<>(environments));

    }
}