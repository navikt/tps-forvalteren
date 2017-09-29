package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;

@RunWith(MockitoJUnitRunner.class)
public class FindPersonerWithoutDoedsmeldingTest {

    private Person aPersonWithDoedsmeldingSent;
    private Person aPersonWithoutDoedsmeldingSent;
    private List<Person> personer;

    @Mock
    private SjekkDoedsmeldingSentForPerson sjekkDoedsmeldingSentForPersonMock;

    @InjectMocks
    private FindPersonerWithoutDoedsmelding findPersonerWithoutDoedsmelding;

    @Before
    public void setup() {
        aPersonWithDoedsmeldingSent = mock(Person.class);
        aPersonWithoutDoedsmeldingSent = mock(Person.class);

        personer = Arrays.asList(aPersonWithDoedsmeldingSent, aPersonWithoutDoedsmeldingSent);

        when(sjekkDoedsmeldingSentForPersonMock.execute(aPersonWithDoedsmeldingSent)).thenReturn(true);
        when(sjekkDoedsmeldingSentForPersonMock.execute(aPersonWithoutDoedsmeldingSent)).thenReturn(false);
    }

    @Test
    public void findPersonWithDoedsmeldingSent() {
        List<Person> result = findPersonerWithoutDoedsmelding.execute(personer);
        System.out.println(result.toString());

        assertThat(result, hasItem(aPersonWithoutDoedsmeldingSent));
        assertThat(result, not(hasItem(aPersonWithDoedsmeldingSent)));
    }
}
