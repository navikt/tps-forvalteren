package no.nav.tps.forvalteren.service.command.testdata.skd;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Arrays.asList;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.common.collect.Sets;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.restreq.PersonService;

@RunWith(MockitoJUnitRunner.class)
public class LagreTilTpsServiceTest {

    private static final String INNVANDRING_MLD = "Innvandring";
    private static final String RELASJON_MLD = "Relasjon";
    private static final String FOEDSELS_MLD = "Foedsel";
    private static final String UTVANDRING_MLD = "Innvandring";
    private static final Long GRUPPE_ID = 1337L;

    private List<SendSkdMeldingTilTpsResponse> innvandringResponse = new ArrayList<>();
    private List<SendSkdMeldingTilTpsResponse> updateInnvadringResponse = new ArrayList<>();
    private List<SendSkdMeldingTilTpsResponse> foedselsmeldingResponse = new ArrayList<>();
    private List<SendSkdMeldingTilTpsResponse> relasjonsResponse = new ArrayList<>();
    private List<SendSkdMeldingTilTpsResponse> doedsmeldingResponse = new ArrayList<>();
    private List<SendSkdMeldingTilTpsResponse> vergemaalsresponse = new ArrayList<>();
    private List<SendSkdMeldingTilTpsResponse> utvandringsResponse = new ArrayList<>();

    @Mock
    private FindPersonsNotInEnvironments findPersonsNotInEnvironments;

    @Mock
    private FindPersonerSomSkalHaFoedselsmelding findPersonerSomSkalHaFoedselsmelding;

    @Mock
    private SendNavEndringsmeldinger sendNavEndringsmeldinger;

    @Mock
    private FindGruppeById findGruppeByIdMock;

    @Mock
    private SkdMeldingSender skdMeldingSender;

    @Mock
    private PersonService personService;

    @Mock
    private PersonStatusFraMiljoService personStatusFraMiljoService;

    @InjectMocks
    private LagreTilTpsService lagreTilTpsService;

    private List<Person> persons = new ArrayList<>();
    private List<Person> personsInGruppe = new ArrayList<>();
    private Gruppe gruppe = Gruppe.builder().personer(personsInGruppe).build();
    private Person person = aMalePerson().build();
    private Set<String> environments = Sets.newHashSet("u2", "env", "env2");

    private Map<String, String> expectedStatus = new HashMap<>();
    private Map<String, String> tpsResponseStatus = new HashMap<>();

    {
        persons.add(person);
        expectedStatus.put("env", "OK");
        expectedStatus.put("env2", "persistering feilet");
        expectedStatus.put("u2", "Environment is not deployed");
        gruppe.getPersoner().add(person);
        tpsResponseStatus.put("env", "00");
        tpsResponseStatus.put("env2", "persistering feilet");
    }

    // TODO Mangelfull testing her må rettes opp

    @Before
    public void setup() {
        when(findGruppeByIdMock.execute(GRUPPE_ID)).thenReturn(gruppe);
        when(findPersonerSomSkalHaFoedselsmelding.execute(personsInGruppe)).thenReturn(persons);

        when(skdMeldingSender.sendInnvandringsMeldinger(anyList(), anySet())).thenReturn(innvandringResponse);
        when(skdMeldingSender.sendUpdateInnvandringsMeldinger(anyList(), anySet())).thenReturn(updateInnvadringResponse);
        when(skdMeldingSender.sendFoedselsMeldinger(anyList(), anySet())).thenReturn(foedselsmeldingResponse);
        when(skdMeldingSender.sendRelasjonsmeldinger(anyList(), anySet())).thenReturn(relasjonsResponse);
        when(skdMeldingSender.sendDoedsmeldinger(anyList(), anySet())).thenReturn(doedsmeldingResponse);
        when(skdMeldingSender.sendVergemaalsmeldinger(anyList(), anySet())).thenReturn(vergemaalsresponse);
        when(skdMeldingSender.sendUtvandringOgNyeInnvandringsmeldinger(anyList(), anySet())).thenReturn(utvandringsResponse);
    }

    @Test
    public void checkThatServicesGetsCalled() throws Exception {
        innvandringResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("123").build());
        when(skdMeldingSender.sendInnvandringsMeldinger(anyList(), anySet())).thenReturn(
                        List.of(SendSkdMeldingTilTpsResponse.builder().skdmeldingstype(INNVANDRING_MLD)
                                .status(newHashMap(Map.of("env","OK"))).build()));

        lagreTilTpsService.execute(GRUPPE_ID, environments);

        verify(skdMeldingSender, times(3)).sendInnvandringsMeldinger(any(), anySet());
        verify(skdMeldingSender, times(3)).sendUpdateInnvandringsMeldinger(anyList(), anySet());
        verify(skdMeldingSender, times(3)).sendFoedselsMeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendUtvandringOgNyeInnvandringsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendRelasjonsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendDoedsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendVergemaalsmeldinger(anyList(), anySet());

        ArgumentCaptor<Set> captor = ArgumentCaptor.forClass(Set.class);
        verify(sendNavEndringsmeldinger).execute(anyList(), captor.capture());

        Set<String> miljoer = captor.getValue();

        assertThat(miljoer.contains("u2"), is(true));
        assertThat(miljoer.contains("env2"), is(true));
        assertThat(miljoer.contains("env"), is(true));
    }

    @Test
    public void shouldReturnResponsesWithStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("u2", "00");

        innvandringResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("1").skdmeldingstype(INNVANDRING_MLD).status(status).build());
        foedselsmeldingResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("2").skdmeldingstype(FOEDSELS_MLD).status(status).build());
        utvandringsResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("3").skdmeldingstype(UTVANDRING_MLD).status(status).build());
        relasjonsResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("4").skdmeldingstype(RELASJON_MLD).status(status).build());

        RsSkdMeldingResponse actualResponse = lagreTilTpsService.execute(GRUPPE_ID, environments);

        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().size(), is(4));
        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().get(0).getPersonId(), is("1"));
        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().get(1).getPersonId(), is("2"));
        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().get(2).getPersonId(), is("4"));
        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().get(3).getPersonId(), is("3"));

        assertThat(status, is(actualResponse.getSendSkdMeldingTilTpsResponsene().get(0).getStatus()));
        assertThat(asList(INNVANDRING_MLD, FOEDSELS_MLD, RELASJON_MLD, UTVANDRING_MLD),
                is(actualResponse.getSendSkdMeldingTilTpsResponsene()
                        .stream()
                        .map(SendSkdMeldingTilTpsResponse::getSkdmeldingstype)
                        .collect(Collectors.toList())));
    }
}
