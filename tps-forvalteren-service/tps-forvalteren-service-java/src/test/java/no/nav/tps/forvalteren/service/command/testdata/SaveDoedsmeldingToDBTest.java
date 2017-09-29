package no.nav.tps.forvalteren.service.command.testdata;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;

@RunWith(MockitoJUnitRunner.class)
public class SaveDoedsmeldingToDBTest {

    private List<Person> personer;

    @Mock
    private DoedsmeldingRepository doedsmeldingRepositoryMock;

    @InjectMocks
    private SaveDoedsmeldingToDB saveDoedsmeldingToDB;

    @Captor
    private ArgumentCaptor<List<Doedsmelding>> doedsmeldingerCaptor;

    @Before
    public void setup() {
        Person person1 = mock(Person.class);
        Person person2 = mock(Person.class);
        Person person3 = mock(Person.class);

        personer = Arrays.asList(person1, person2, person3);
    }

    @Test
    public void allDoedsmeldingerAreSavedToRepository() {
        saveDoedsmeldingToDB.execute(personer);

        verify(doedsmeldingRepositoryMock).save(doedsmeldingerCaptor.capture());
        assertEquals(doedsmeldingerCaptor.getValue().size(), personer.size());
    }
}
