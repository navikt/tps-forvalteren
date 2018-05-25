package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.domain.rs.skd.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.InnvandringAarsakskode02.INNVANDRING_CREATE_MLD_NAVN;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LagreTilTpsTest {

	private static final boolean ADD_HEADER = true;
	private static final Long GRUPPE_ID = 1337L;
	private static final String melding1 = "11111111111111", melding2 = "222222222222", melding3 = "33333333333";

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
	@Mock
	private FindGruppeById findGruppeByIdMock;
	@Mock
	private SendNavEndringsmelding sendNavEndringsmelding;

	@InjectMocks
	private LagreTilTps lagreTilTps;
	private List<Person> persons = new ArrayList<>();
	private List<Person> personsInGruppe = new ArrayList<>();
	private Gruppe gruppe = Gruppe.builder().personer(personsInGruppe).build();
	private Person person = aMalePerson().build();
	private List<String> environments = new ArrayList<>();
	private List<String> innvandringsMeldinger = Arrays.asList(melding1);
	private List<String> relasjonsMeldinger = Arrays.asList(melding2);
	private List<String> doedsMeldinger = Arrays.asList(melding3);
	private Map<String, String> expectedStatus = new HashMap<>();
	private Map<String, String> TPSResponse = new HashMap<>();

	{
		persons.add(person);
		environments.add("u2");
		environments.add("env");
		environments.add("env2");
		expectedStatus.put("env", "OK");
		expectedStatus.put("env2", "persistering feilet");
		expectedStatus.put("u2", "Environment is not deployed");
		TPSResponse.put("env", "00");
		TPSResponse.put("env2", "persistering feilet");
	}

	@Before
	public void setup() {

	    when(findGruppeByIdMock.execute(GRUPPE_ID)).thenReturn(gruppe);
		when(findPersonsNotInEnvironments.execute(personsInGruppe, environments)).thenReturn(persons);
		when(skdMessageCreatorTrans1.execute(INNVANDRING_CREATE_MLD_NAVN, persons, ADD_HEADER)).thenReturn(innvandringsMeldinger);
		when(createRelasjoner.execute(persons, ADD_HEADER)).thenReturn(relasjonsMeldinger);
		when(createDoedsmeldinger.execute(GRUPPE_ID, ADD_HEADER)).thenReturn(doedsMeldinger);
		when(innvandring.resolve()).thenReturn(skdRequestMeldingDefinition);
	}

	@Test
	public void checkThatServicesGetsCalled() {
		lagreTilTps.execute(GRUPPE_ID, environments);

		verify(findPersonsNotInEnvironments).execute(personsInGruppe, environments);
		verify(skdMessageCreatorTrans1).execute(INNVANDRING_CREATE_MLD_NAVN, persons, ADD_HEADER);
		verify(createRelasjoner).execute(persons, ADD_HEADER);
		verify(createDoedsmeldinger).execute(GRUPPE_ID, ADD_HEADER);
		verify(innvandring).resolve();
		verify(sendSkdMeldingTilGitteMiljoer).execute(melding1, skdRequestMeldingDefinition, new HashSet<>(environments));
		verify(sendNavEndringsmelding).execute(personsInGruppe, new HashSet<>(environments));

	}

	/**
	 * Testbetingelser:
	 * - HVIS miljøet ikke er deployet, skal status være "Environment is not deployed".
	 * - HVIS persistering til TPS i et gitt miljø går ok, skal status være "OK".
	 * - HVIS persistering til TPS feiler, så skal responsen fra TPS returneres.
	 * - Responsen skal inneholde skdMeldingstypene som ble sendt, gruppeId og personId-ene for å identifisere skdMeldingene.
	 */
	@Test
	public void shouldReturnResponsesWithStatus() {
		when(sendSkdMeldingTilGitteMiljoer.execute(any(), any(), any())).thenReturn(TPSResponse);
		RsSkdMeldingResponse actualResponse = lagreTilTps.execute(GRUPPE_ID, environments);
        assertEquals(expectedStatus, actualResponse.getSendSkdMeldingTilTpsResponsene().get(0).getStatus());
		assertEquals(Arrays.asList(INNVANDRING_CREATE_MLD_NAVN, "Relasjonsmelding", "Doedsmelding"),
				actualResponse.getSendSkdMeldingTilTpsResponsene()
				.stream()
				.map(SendSkdMeldingTilTpsResponse::getSkdmeldingstype)
				.collect(Collectors.toList()));
		assertEquals(GRUPPE_ID,actualResponse.getGruppeid());
		assertEquals(melding1.substring(0,11),actualResponse.getSendSkdMeldingTilTpsResponsene().get(0).getPersonId());
	}
}
