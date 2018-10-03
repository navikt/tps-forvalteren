package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.FindGruppeById;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonerSomSkalHaFoedselsmelding;
import no.nav.tps.forvalteren.service.command.testdata.FindPersonsNotInEnvironments;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.RsSkdMeldingResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps.SendSkdMeldingTilTpsResponse;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.mockito.runners.MockitoJUnitRunner;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LagreTilTpsServiceTest {

    private static final String INNVANDRING_MLD = "Innvandring";
    private static final String RELASJON_MLD = "Relasjon";
    private static final String FOEDSELS_MLD = "Foedsel";
    private static final String UTVANDRING_MLD = "Innvandring";
    private static final Long GRUPPE_ID = 1337L;
    private static final String melding1 = "11111111111111", melding2 = "222222222222", melding3 = "33333333333", melding4 = "44444444444";
    List<SendSkdMeldingTilTpsResponse> innvandringResponse = new ArrayList<>();
    List<SendSkdMeldingTilTpsResponse> updateInnvadringResponse = new ArrayList<>();
    List<SendSkdMeldingTilTpsResponse> foedselsmeldingResponse = new ArrayList<>();
    List<SendSkdMeldingTilTpsResponse> relasjonsResponse = new ArrayList<>();
    List<SendSkdMeldingTilTpsResponse> doedsmeldingResponse = new ArrayList<>();
    List<SendSkdMeldingTilTpsResponse> vergemaalsresponse = new ArrayList<>();
    List<SendSkdMeldingTilTpsResponse> utvandringsResponse = new ArrayList<>();

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

    @InjectMocks
    private LagreTilTpsService lagreTilTpsService;
    private List<Person> persons = new ArrayList<>();
    private List<Person> personsInGruppe = new ArrayList<>();
    private Gruppe gruppe = Gruppe.builder().personer(personsInGruppe).build();
    private Person person = aMalePerson().build();
    private List<String> environments = new ArrayList<>();
    private List<SkdMeldingTrans1> innvandringsMeldinger = Arrays.asList(SkdMeldingTrans1.builder().fornavn(melding1).fodselsdato("110218").personnummer("12345").build());

    private Map<String, String> expectedStatus = new HashMap<>();
    private Map<String, String> tpsResponseStatus = new HashMap<>();
    {
        persons.add(person);
        environments.add("u2");
        environments.add("env");
        environments.add("env2");
        expectedStatus.put("env", "OK");
        expectedStatus.put("env2", "persistering feilet");
        expectedStatus.put("u2", "Environment is not deployed");
        tpsResponseStatus.put("env", "00");
        tpsResponseStatus.put("env2", "persistering feilet");
    }

    // TODO Mangelfull testing her må rettes opp

    @Before
    public void setup() {
        when(findGruppeByIdMock.execute(GRUPPE_ID)).thenReturn(gruppe);
        when(findPersonsNotInEnvironments.execute(personsInGruppe, environments)).thenReturn(persons);
        when(findPersonerSomSkalHaFoedselsmelding.execute(personsInGruppe)).thenReturn(persons);

        when(skdMeldingSender.sendInnvandringsMeldinger(anyList(), anySet())).thenReturn(innvandringResponse);
        when(skdMeldingSender.sendUpdateInnvandringsMeldinger(anyList(), anySet())).thenReturn(updateInnvadringResponse);
        when(skdMeldingSender.sendFoedselsMeldinger(anyList(), anySet())).thenReturn(foedselsmeldingResponse);
        when(skdMeldingSender.sendRelasjonsmeldinger(anyList(), anySet())).thenReturn(relasjonsResponse);
        when(skdMeldingSender.sendDoedsmeldinger(anyList(), anySet())).thenReturn(doedsmeldingResponse);
        when(skdMeldingSender.sendVergemaalsmeldinger(anyList(), anySet())).thenReturn(vergemaalsresponse);
        when(skdMeldingSender.sendUtvandringsmeldinger(anyList(), anySet())).thenReturn(utvandringsResponse);
    }

    @Test
    public void checkThatServicesGetsCalled() {
        innvandringResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("123").build());
        lagreTilTpsService.execute(GRUPPE_ID, environments);

        verify(skdMeldingSender).sendInnvandringsMeldinger(any(), anySet());
        verify(skdMeldingSender).sendUpdateInnvandringsMeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendFoedselsMeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendRelasjonsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendDoedsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendVergemaalsmeldinger(anyList(), anySet());
        verify(skdMeldingSender).sendUtvandringsmeldinger(anyList(), anySet());

        ArgumentCaptor<Set> captor = ArgumentCaptor.forClass(Set.class);
        verify(sendNavEndringsmeldinger).execute(anyList(), captor.capture());

        Set<String> miljoer = captor.getValue();

        assertThat(miljoer.contains("u2"), is(true));
        assertThat(miljoer.contains("env2"), is(true));
        assertThat(miljoer.contains("env"), is(true));
    }

    @Test
    public void shouldReturnResponsesWithStatus() {
        Map<String,String> map = new HashMap<>();
        map.put("u2", "00");

        innvandringResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("1").skdmeldingstype(INNVANDRING_MLD).status(map).build());
        foedselsmeldingResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("2").skdmeldingstype(FOEDSELS_MLD).status(map).build());
        relasjonsResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("3").skdmeldingstype(RELASJON_MLD).status(map).build());
        utvandringsResponse.add(SendSkdMeldingTilTpsResponse.builder().personId("4").skdmeldingstype(UTVANDRING_MLD).status(map).build());

        RsSkdMeldingResponse actualResponse = lagreTilTpsService.execute(GRUPPE_ID, environments);

        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().size(), is(4));
        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().get(0).getPersonId(), is("1"));
        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().get(1).getPersonId(), is("2"));
        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().get(2).getPersonId(), is("3"));
        assertThat(actualResponse.getSendSkdMeldingTilTpsResponsene().get(3).getPersonId(), is("4"));


        assertEquals(map, actualResponse.getSendSkdMeldingTilTpsResponsene().get(0).getStatus());
        assertEquals(Arrays.asList(INNVANDRING_MLD, FOEDSELS_MLD, RELASJON_MLD, UTVANDRING_MLD),
                actualResponse.getSendSkdMeldingTilTpsResponsene()
                        .stream()
                        .map(SendSkdMeldingTilTpsResponse::getSkdmeldingstype)
                        .collect(Collectors.toList()));

    }
}
