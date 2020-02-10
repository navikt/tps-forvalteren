package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeleteDoedsmeldingByPersonIdInTest {

    @Mock
    private DoedsmeldingRepository doedsmeldingRepository;

    @InjectMocks
    private DeleteDoedsmeldingByPersonIdIn deleteDoedsmeldingByPersonIdIn;

    @Mock
    private List<Long> personIds;
    
    @Before
    public void setup() {
        when(personIds.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS);
    }

    @Test
    public void verifyServiceCall() {
        deleteDoedsmeldingByPersonIdIn.execute(personIds);

        verify(doedsmeldingRepository).deleteByPersonIdIn(personIds);
    }

    @Test
    public void verifyServiceCallWithMoreRelasjonerThanMaxInQuery() {
        when(personIds.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS * 10);

        deleteDoedsmeldingByPersonIdIn.execute(personIds);

        verify(doedsmeldingRepository, times(10)).deleteByPersonIdIn(anyList());
    }
    
}