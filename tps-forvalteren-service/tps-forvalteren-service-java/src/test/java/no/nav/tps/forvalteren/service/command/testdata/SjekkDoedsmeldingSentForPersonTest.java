package no.nav.tps.forvalteren.service.command.testdata;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;

@RunWith(MockitoJUnitRunner.class)
public class SjekkDoedsmeldingSentForPersonTest {

    private final Long ID_DOEDSMELDING_AND_SENT = 1L;
    private final Long ID_DOEDSMELDING_AND_NOT_SENT = 2L;
    private final Long ID_NO_DOEDSMELDING = 3L;

    private Person aPersonWithDoedsmeldingAndSent;
    private Person aPersonWithDoedsmeldingAndNotSent;
    private Person aPersonWithoutDoedsmelding;

    @Mock
    private DoedsmeldingRepository doedsmeldingRepositoryMock;

    @InjectMocks
    private SjekkDoedsmeldingSentForPerson sjekkDoedsmeldingSentForPerson;

    @Before
    public void setup() {
        Doedsmelding doedsmeldingThatIsSent = mock(Doedsmelding.class);
        Doedsmelding doedsmeldingThatIsNotSent = mock(Doedsmelding.class);

        aPersonWithDoedsmeldingAndSent = mock(Person.class);
        aPersonWithDoedsmeldingAndNotSent = mock(Person.class);
        aPersonWithoutDoedsmelding = mock(Person.class);

        when(aPersonWithDoedsmeldingAndSent.getId()).thenReturn(ID_DOEDSMELDING_AND_SENT);
        when(aPersonWithDoedsmeldingAndNotSent.getId()).thenReturn(ID_DOEDSMELDING_AND_NOT_SENT);
        when(aPersonWithoutDoedsmelding.getId()).thenReturn(ID_NO_DOEDSMELDING);

        when(doedsmeldingRepositoryMock.findByPersonId(ID_DOEDSMELDING_AND_SENT)).thenReturn(doedsmeldingThatIsSent);
        when(doedsmeldingRepositoryMock.findByPersonId(ID_DOEDSMELDING_AND_NOT_SENT)).thenReturn(doedsmeldingThatIsNotSent);
        when(doedsmeldingRepositoryMock.findByPersonId(ID_NO_DOEDSMELDING)).thenReturn(null);

        when(doedsmeldingThatIsSent.getIsMeldingSent()).thenReturn(true);
        when(doedsmeldingThatIsNotSent.getIsMeldingSent()).thenReturn(false);
    }

    @Test
    public void testPersonWithDoedsmeldingAndSent() {
        assertThat(sjekkDoedsmeldingSentForPerson.execute(aPersonWithDoedsmeldingAndSent), is(true));
    }

    @Test
    public void testPersonWithDoedsmeldingAndNotSent() {
        assertThat(sjekkDoedsmeldingSentForPerson.execute(aPersonWithDoedsmeldingAndNotSent), is(false));
    }

    @Test
    public void testPersonWithoutDoedsmelding() {
        assertThat(sjekkDoedsmeldingSentForPerson.execute(aPersonWithoutDoedsmelding), is(false));
    }
}
