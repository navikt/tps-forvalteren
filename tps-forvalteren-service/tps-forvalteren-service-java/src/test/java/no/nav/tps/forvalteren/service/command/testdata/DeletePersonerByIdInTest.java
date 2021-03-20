package no.nav.tps.forvalteren.service.command.testdata;

import static no.nav.tps.forvalteren.service.command.testdata.utils.TestdataConstants.ORACLE_MAX_IN_SET_ELEMENTS;
import static org.mockito.ArgumentMatchers.anyList;
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

    @Mock
    private EntityManagerFactory entityManagerFactory;

    private DeletePersonerByIdIn deletePersonerByIdIn;

    @Mock
    private List<Long> personIds;

    @Before
    public void setup() {
        deletePersonerByIdIn = new DeletePersonerByIdIn(entityManagerFactory, personRepository,
                relasjonRepository, doedsmeldingRepository);
        when(personIds.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS);
    }

    @Test
    public void verifyAllServices() {
        deletePersonerByIdIn.execute(personIds);

        verify(doedsmeldingRepository).deleteByPersonIdIn(anyList());
        verify(relasjonRepository).deleteByPersonRelasjonMedIdIn(anySet());
        verify(personRepository).deleteByIdIn(anyList());
    }

    @Test
    public void verifyAllServicesWithMorePersonsThanOracleLimit() {
        when(personIds.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS * 10);

        deletePersonerByIdIn.execute(personIds);

        verify(doedsmeldingRepository, times(10)).deleteByPersonIdIn(anyList());
        verify(relasjonRepository, times(10)).deleteByPersonRelasjonMedIdIn(anySet());
        verify(personRepository, times(10)).deleteByIdIn(anyList());
    }
}