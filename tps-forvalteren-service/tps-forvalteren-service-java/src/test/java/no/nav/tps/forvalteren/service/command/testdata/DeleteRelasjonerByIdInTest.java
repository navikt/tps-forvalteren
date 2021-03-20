package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

@RunWith(MockitoJUnitRunner.class)
public class DeleteRelasjonerByIdInTest {

    @Mock
    private RelasjonRepository relasjonRepository;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    private DeleteRelasjonerByIdIn deleteRelasjonerByIdIn;
    
    @Mock
    private List<Long> personIds;
    
    @Before
    public void setup() {
        deleteRelasjonerByIdIn = new DeleteRelasjonerByIdIn(entityManagerFactory, relasjonRepository);
        when(personIds.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS);
    }
    
    @Test
    public void verifyServiceCall() {
        deleteRelasjonerByIdIn.execute(personIds);

        verify(relasjonRepository).deleteByPersonRelasjonMedIdIn(anySet());
    }

    @Test
    public void verifyServiceCallWithMoreRelasjonerThanMaxInQuery() {
        when(personIds.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS * 10);

        deleteRelasjonerByIdIn.execute(personIds);

        verify(relasjonRepository, times(10)).deleteByPersonRelasjonMedIdIn(anySet());
    }
}