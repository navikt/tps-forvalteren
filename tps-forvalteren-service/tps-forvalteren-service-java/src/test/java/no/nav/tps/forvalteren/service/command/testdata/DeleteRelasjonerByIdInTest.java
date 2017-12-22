package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;
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

import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeleteRelasjonerByIdInTest {

    @Mock
    private RelasjonRepository relasjonRepository;
    
    @InjectMocks
    private DeleteRelasjonerByIdIn deleteRelasjonerByIdIn;
    
    @Mock
    private List<Long> personIds;
    
    @Before
    public void setup() {
        when(personIds.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS);
    }
    
    @Test
    public void verifyServiceCall() {
        deleteRelasjonerByIdIn.execute(personIds);
        
        verify(relasjonRepository).deleteByPersonRelasjonMedIdIn(personIds);
    }

    @Test
    public void verifyServiceCallWithMoreRelasjonerThanMaxInQuery() {
        when(personIds.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS * 10);
        
        deleteRelasjonerByIdIn.execute(personIds);
        
        verify(relasjonRepository, times(10)).deleteByPersonRelasjonMedIdIn(anyListOf(Long.class));
    }
    
}