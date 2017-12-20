package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_SUM_IN_QUERY;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeletePersonerByIdInTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private DoedsmeldingRepository doedsmeldingRepository;

    @InjectMocks
    private DeletePersonerByIdIn deletePersonerByIdIn;

    @Mock
    private List<Long> personIds;

    @Before
    public void setup() {
        when(personIds.size()).thenReturn(ORACLE_MAX_SUM_IN_QUERY);
    }

    @Test
    public void verifyAllServices() {
        deletePersonerByIdIn.execute(personIds);

        verify(doedsmeldingRepository).deleteByPersonIdIn(personIds);
        verify(relasjonRepository).deleteByPersonRelasjonMedIdIn(personIds);
        verify(personRepository).deleteByIdIn(personIds);
    }

    @Test
    public void verifyAllServicesWithMorePersonsThanOracleLimit() {
        when(personIds.size()).thenReturn(ORACLE_MAX_SUM_IN_QUERY * 10);

        deletePersonerByIdIn.execute(personIds);

        verify(doedsmeldingRepository, times(10)).deleteByPersonIdIn(anyListOf(Long.class));
        verify(relasjonRepository, times(10)).deleteByPersonRelasjonMedIdIn(anyListOf(Long.class));
        verify(personRepository, times(10)).deleteByIdIn(anyListOf(Long.class));
    }
}