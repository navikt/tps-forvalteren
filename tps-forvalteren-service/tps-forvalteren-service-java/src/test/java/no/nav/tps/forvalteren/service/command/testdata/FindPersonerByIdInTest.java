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

import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@RunWith(MockitoJUnitRunner.class)
public class FindPersonerByIdInTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private FindPersonerByIdIn findPersonerByIdIn;

    @Mock
    private List<String> identer;

    @Before
    public void setup() {
        when(identer.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS);
    }

    @Test
    public void checkThatRepostioryGetsCalled() {
        findPersonerByIdIn.execute(identer);

        verify(personRepository).findByIdentIn(identer);
    }

    @Test
    public void checkThatRepostioryGetsCalledMultipleTimes() {
        when(identer.size()).thenReturn(ORACLE_MAX_IN_SET_ELEMENTS * 10);

        findPersonerByIdIn.execute(identer);

        verify(personRepository, times(10)).findByIdentIn(anyListOf(String.class));
    }

}