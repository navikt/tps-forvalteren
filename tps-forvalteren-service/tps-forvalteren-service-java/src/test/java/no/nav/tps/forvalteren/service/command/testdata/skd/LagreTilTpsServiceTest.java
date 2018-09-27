package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.testdata.UppercaseDataInPerson;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;

@RunWith(MockitoJUnitRunner.class)
public class LagreTilTpsServiceTest {

    private static final boolean ADD_HEADER = true;
    private static final Long GRUPPE_ID = 1337L;
    private static final String melding1 = "11111111111111", melding2 = "222222222222", melding3 = "33333333333", melding4 = "44444444444";

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;
    @Mock
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;
    @Mock
    private FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;
    @Mock
    private CreateRelasjoner createRelasjoner;
    @Mock
    private CreateDoedsmeldinger createDoedsmeldinger;
    @Mock
    private CreateFoedselsmeldinger createFoedselsmeldinger;
    @Mock
    private CreateUtvandring createUtvandring;
    @Mock
    private CreateVergemaal createVergemaal;
    @Mock
    private SkdMeldingResolver innvandring;
    @Mock
    private SendNavEndringsmeldinger sendNavEndringsmeldinger;
    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;
    @Mock
    private TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition;
    @Mock
    private FindGruppeById findGruppeByIdMock;

    @Mock
    private UppercaseDataInPerson uppercaseDataInPerson;

    @Mock
    private SkdMeldingSender skdMeldingSender;

    @InjectMocks
    private LagreTilTpsService lagreTilTpsService;
    private List<Person> persons = new ArrayList<>();
    private List<Person> personsInGruppe = new ArrayList<>();
    private Gruppe gruppe = Gruppe.builder().personer(personsInGruppe).build();
    private Person person = aMalePerson().build();
    private List<String> environments = new ArrayList<>();
    private List<SkdMeldingTrans1> innvandringsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding1).fodselsdato("110218").personnummer("12345").build());
    private List<SkdMelding> relasjonsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding2).build());
    private List<SkdMeldingTrans1> doedsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding3).build());
    private List<SkdMeldingTrans1> utvandringsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding1).fodselsdato("121200").personnummer("98765").build());
    private List<SkdMeldingTrans1> vergemaalsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding4).build());

    private Map<String, String> expectedStatus = new HashMap<>();
    private Map<String, String> tpsResponse = new HashMap<>();

    {
        persons.add(person);
        environments.add("u2");
        environments.add("env");
        environments.add("env2");
        expectedStatus.put("env", "OK");
        expectedStatus.put("env2", "persistering feilet");
        expectedStatus.put("u2", "Environment is not deployed");
        tpsResponse.put("env", "00");
        tpsResponse.put("env2", "persistering feilet");
    }

    // TODO Mangelfull testing her må rettes opp

    @Before
    public void setup() {

        when(findGruppeByIdMock.execute(GRUPPE_ID)).thenReturn(gruppe);
        when(findPersonsNotInEnvironments.execute(personsInGruppe, environments)).thenReturn(persons);
        when(skdMessageCreatorTrans1.execute(INNVANDRING_CREATE_MLD_NAVN, persons, ADD_HEADER)).thenReturn(innvandringsMeldinger);
        when(createRelasjoner.execute(persons, ADD_HEADER)).thenReturn(relasjonsMeldinger);
        when(findPersonerSomSkalHaFoedselsmelding.execute(personsInGruppe)).thenReturn(persons);
        when(createDoedsmeldinger.execute(personsInGruppe, ADD_HEADER)).thenReturn(doedsMeldinger);
        when(createUtvandring.execute(persons, ADD_HEADER)).thenReturn(utvandringsMeldinger);
        when(innvandring.resolve()).thenReturn(skdRequestMeldingDefinition);
        when(createVergemaal.execute(personsInGruppe, ADD_HEADER)).thenReturn(vergemaalsMeldinger);
    }

    @Test
    @Ignore
    public void checkThatServicesGetsCalled() {
        lagreTilTpsService.execute(GRUPPE_ID, environments);

        verify(findPersonsNotInEnvironments).execute(personsInGruppe, environments);
        verify(skdMessageCreatorTrans1).execute(INNVANDRING_CREATE_MLD_NAVN, persons, ADD_HEADER);
        verify(createRelasjoner).execute(persons, ADD_HEADER);
        verify(createDoedsmeldinger).execute(persons, ADD_HEADER);
        verify(createUtvandring).execute(personsInGruppe, ADD_HEADER);
        verify(innvandring).resolve();
        verify(createFoedselsmeldinger).executeFromPersons(persons, ADD_HEADER);
        verify(sendSkdMeldingTilGitteMiljoer).execute(innvandringsMeldinger.get(0).toString(), skdRequestMeldingDefinition, new HashSet<>(environments));
        verify(createVergemaal).execute(personsInGruppe, ADD_HEADER);
    }

    /**
     * Testbetingelser:
     * - HVIS miljøet ikke er deployet, skal status være "Environment is not deployed".
     * - HVIS persistering til TPS i et gitt miljø går ok, skal status være "OK".
     * - HVIS persistering til TPS feiler, så skal responsen fra TPS returneres.
     * - Responsen skal inneholde skdMeldingstypene som ble sendt, gruppeId og personId-ene for å identifisere skdMeldingene.
     */
    @Test
    @Ignore
    public void shouldReturnResponsesWithStatus() {
        when(sendSkdMeldingTilGitteMiljoer.execute(any(), any(), any())).thenReturn(tpsResponse);
        RsSkdMeldingResponse actualResponse = lagreTilTpsService.execute(GRUPPE_ID, environments);
        assertEquals(expectedStatus, actualResponse.getSendSkdMeldingTilTpsResponsene().get(0).getStatus());
        assertEquals(Arrays.asList(INNVANDRING_CREATE_MLD_NAVN, "Relasjonsmelding", "Doedsmelding", "Vergemaal", "Utvandring"),
                actualResponse.getSendSkdMeldingTilTpsResponsene()
                        .stream()
                        .map(SendSkdMeldingTilTpsResponse::getSkdmeldingstype)
                        .collect(Collectors.toList()));
        assertEquals(GRUPPE_ID, actualResponse.getGruppeid());
        assertEquals(innvandringsMeldinger.get(0).getFodselsnummer(), actualResponse.getSendSkdMeldingTilTpsResponsene().get(0).getPersonId());
    }
}
