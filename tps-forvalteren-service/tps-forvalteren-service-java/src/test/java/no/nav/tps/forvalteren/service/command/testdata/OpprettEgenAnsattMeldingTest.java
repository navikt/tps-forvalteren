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
public class OpprettEgenAnsattMeldingTest {

    @Mock
    private RsTpsRequestMappingUtils mappingUtils;

    @InjectMocks
    private OpprettEgenAnsattMelding opprettEgenAnsattMelding;

    @Mock
    private TpsServiceRoutineRequest tpsServiceRoutineRequest;

    @Mock
    private Person person;

    @Test
    public void execute() {

        person = new Person();
        LocalDateTime localDateTimeFom = LocalDateTime.of(2018, 06, 01, 12, 00);
        LocalDateTime localDateTimeTom = LocalDateTime.of(2020, 01, 01, 12, 00);
        String stringLocalDateTimeFom = "2018-06-01";
        String stringLocalDateTimeTom = "2020-01-01";

        person.setIdent("11111100000");
        person.setEgenAnsattDatoFom(localDateTimeFom);
        person.setEgenAnsattDatoTom(localDateTimeTom);

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        when(mappingUtils.convertToTpsServiceRoutineRequest(anyString(), anyMap())).thenReturn(tpsServiceRoutineRequest);

        List<TpsNavEndringsMelding> result = opprettEgenAnsattMelding.execute(person, new HashSet(Arrays.asList("u5")));
        verify(mappingUtils).convertToTpsServiceRoutineRequest(anyString(), captor.capture());

        assertThat(result, is(instanceOf(List.class)));
        assertThat(captor.getValue().get("fom"), is(stringLocalDateTimeFom));
        assertThat(captor.getValue().get("serviceRutinenavn"), is("endre_egen_ansatt"));
        assertThat(captor.getValue().get("offentligIdent"), is("11111100000"));

    }
}
