package no.nav.tps.forvalteren.service.command.testdata.skd;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02;
import no.nav.tps.forvalteren.domain.test.provider.PersonProvider;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.GetSkdMeldingByName;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersCreatorService;

@RunWith(MockitoJUnitRunner.class)
public class SkdMessageCreatorTrans1Test {

    private final static boolean ADD_HEADER = true;
    private final static String VIGSEL = "Vigsel";
    private final static String INNVANDRING = "Innvandring";

    private Person person = PersonProvider.aMalePerson().build();
    private Person person2 = PersonProvider.aFemalePerson().build();
    private Person person3 = PersonProvider.aChildPerson().build();
    private List<Person> persons = new ArrayList<>();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Mock
    private SkdParametersCreatorService skdParametersCreatorService;

    @Mock
    private GetSkdMeldingByName getSkdMeldingByName;

    @Mock
    private GenerateSkdMelding generateSkdMelding;

    @Test
    public void illegalSkdMeldingByNameThrowsException() {
        expectedException.expect(IllegalArgumentException.class);
        when(getSkdMeldingByName.execute(anyString())).thenReturn(Optional.empty());
        persons.add(person);

        skdMessageCreatorTrans1.execute(VIGSEL, persons, ADD_HEADER);
    }

    @Test
    public void checkThatGenerateSkdMeldingGetsCalledMultipleTimes() {
        persons.add(person);
        persons.add(person2);
        persons.add(person3);

        TpsSkdRequestMeldingDefinition tpsSkdRequestMeldingDefinition = new InnvandringAarsakskode02().resolve();
        Optional<TpsSkdRequestMeldingDefinition> skdRequestMeldingDefinitionOptional = Optional.of(tpsSkdRequestMeldingDefinition);

        when(getSkdMeldingByName.execute(INNVANDRING)).thenReturn(skdRequestMeldingDefinitionOptional);

        skdMessageCreatorTrans1.execute(INNVANDRING, persons, ADD_HEADER);

        verify(generateSkdMelding).execute(skdRequestMeldingDefinitionOptional.get(), person, ADD_HEADER);
        verify(generateSkdMelding).execute(skdRequestMeldingDefinitionOptional.get(), person2, ADD_HEADER);
        verify(generateSkdMelding).execute(skdRequestMeldingDefinitionOptional.get(), person3, ADD_HEADER);
    }
}