package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FindAllPersonServiceTest {

    @Mock
    private PersonRepository repositoryMock;

    @Mock
    private List<Person> personResultMock;

    @InjectMocks
    private FindAllPersonService command;

    @Before
    public void setup() {
        when(repositoryMock.findAllByOrderByIdAsc()).thenReturn(personResultMock);
    }

    @Test
    public void callsPersonRepository() {
        command.execute();
        verify(repositoryMock).findAllByOrderByIdAsc();
    }

    @Test
    public void returnsAllPersonsFromRepository() {
        List<Person> result = repositoryMock.findAllByOrderByIdAsc();
        assertThat(result, is(sameInstance(personResultMock)));
    }

}
