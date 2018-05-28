package no.nav.tps.forvalteren.service.command.testdata;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.testdata.skd.TpsNavEndringsMelding;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpprettSikkerhetstiltakMeldingTest {

    @Mock
    private RsTpsRequestMappingUtils mappingUtils;

    @InjectMocks
    private OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;

    @Mock
    private TpsServiceRoutineRequest tpsServiceRoutineRequest;

    @Mock
    private Person person;

    private String stringLocalDateTimeFom;
    private String stringLocalDateTimeTom;
    private ArgumentCaptor<Map> captor;

    @Before
    public void setup() {

        person = new Person();

        LocalDateTime localDateTimeFom = LocalDateTime.of(2018, 06, 01, 12, 00);
        LocalDateTime localDateTimeTom = LocalDateTime.of(2020, 01, 01, 12, 00);

        stringLocalDateTimeFom = "2018-06-01";
        stringLocalDateTimeTom = "2020-01-01";

        person.setIdent("11111100000");
        person.setSikkerhetsTiltakDatoFom(localDateTimeFom);
        person.setSikkerhetsTiltakDatoTom(localDateTimeTom);
        person.setTypeSikkerhetsTiltak("ABCD");
        person.setBeskrSikkerhetsTiltak("en beskrivelse");

        captor = ArgumentCaptor.forClass(Map.class);

        when(mappingUtils.convertToTpsServiceRoutineRequest(anyString(), anyMap())).thenReturn(tpsServiceRoutineRequest);

    }

    @Test
    public void opprettSikkerhetsTiltaksMeldingTest() {

        List<TpsNavEndringsMelding> result = opprettSikkerhetstiltakMelding.execute(person, new HashSet(Arrays.asList("u5")));
        verify(mappingUtils).convertToTpsServiceRoutineRequest(anyString(), captor.capture());

        assertThat(result, is(instanceOf(List.class)));
        assertThat(captor.getValue().get("fom"), is(stringLocalDateTimeFom));
        assertThat(captor.getValue().get("tom"), is(stringLocalDateTimeTom));
        assertThat(captor.getValue().get("offentligIdent"), is("11111100000"));
        assertThat(captor.getValue().get("typeSikkerhetsTiltak"), is("ABCD"));
        assertThat(captor.getValue().get("serviceRutinenavn"), is("endre_sikkerhetstiltak"));

    }

    @Test
    public void opprettSikkerhetsTiltaksMeldingIFlereMiljoTest() {

        List<TpsNavEndringsMelding> result = opprettSikkerhetstiltakMelding.execute(person, new HashSet(Arrays.asList("u5", "t9", "q8", "t2")));

        assertThat(result.size(), is(4));
        assertThat(result.get(0).getMiljo(), is("u5"));
        assertThat(result.get(1).getMiljo(), is("t9"));
        assertThat(result.get(3).getMiljo(), is("t2"));
    }
}
