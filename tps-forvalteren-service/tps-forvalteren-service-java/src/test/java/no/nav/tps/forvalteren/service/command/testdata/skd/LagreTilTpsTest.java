package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;

@RunWith(MockitoJUnitRunner.class)
public class LagreTilTpsTest {

    private static final String NAVN_INNVANDRINGSMELDING = "Innvandring";
    private static final boolean ADD_HEADER = true;

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Mock
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Mock
    private CreateRelasjoner createRelasjoner;

    @Mock
    private CreateDoedsmeldinger createDoedsmeldinger;

    @Mock
    private SkdMeldingResolver innvandring;

    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Mock
    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;

    @InjectMocks
    private LagreTilTps lagreTilTps;

    private List<Person> persons = new ArrayList<>();
    private Person person = aMalePerson().build();
    private List<String> environments = new ArrayList<>();
    private static final Long GRUPPE_ID = 1337L;
    private static final String melding1 = "1", melding2 = "2", melding3 = "3";
    private List<String> innvandringsMeldinger = Arrays.asList(melding1);
    private List<String> relasjonsMeldinger = Arrays.asList(melding2);
    private List<String> doedsMeldinger = Arrays.asList(melding3);

    @Before
    public void setup() {
//        persons.add(person);
//        environments.add("u2");
//        when(findPersonsNotInEnvironments.execute(GRUPPE_ID, environments)).thenReturn(persons);
//        when(skdMessageCreatorTrans1.execute(NAVN_INNVANDRINGSMELDING, persons, ADD_HEADER)).thenReturn(innvandringsMeldinger);
//        when(createRelasjoner.execute(persons, ADD_HEADER)).thenReturn(relasjonsMeldinger);
//        when(createDoedsmeldinger.execute(GRUPPE_ID, ADD_HEADER)).thenReturn(doedsMeldinger);
//        when(innvandring.resolve()).thenReturn(skdRequestMeldingDefinition);
    }

    @Test
    public void checkThatServicesGetsCalled() {
//        lagreTilTps.execute(GRUPPE_ID, environments);
//
//        verify(findPersonsNotInEnvironments).execute(GRUPPE_ID, environments);
//        verify(skdMessageCreatorTrans1).execute(NAVN_INNVANDRINGSMELDING, persons, ADD_HEADER);
//        verify(createRelasjoner).execute(persons, ADD_HEADER);
//        verify(createDoedsmeldinger).execute(GRUPPE_ID, ADD_HEADER);
//        verify(innvandring).resolve();
//        verify(sendSkdMeldingTilGitteMiljoer).execute(melding1, skdRequestMeldingDefinition, new HashSet<>(environments));
    }
}
