package no.nav.tps.forvalteren.service.command.testdata;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

        when(doedsmeldingThatIsSent.getMeldingSendt()).thenReturn(true);
        when(doedsmeldingThatIsNotSent.getMeldingSendt()).thenReturn(false);
    }

    @Test
    public void testPersonWithDoedsmeldingAndSent() {
        assertTrue(sjekkDoedsmeldingSentForPerson.execute(aPersonWithDoedsmeldingAndSent));
    }

    @Test
    public void testPersonWithDoedsmeldingAndNotSent() {
        assertFalse(sjekkDoedsmeldingSentForPerson.execute(aPersonWithDoedsmeldingAndNotSent));
    }

    @Test
    public void testPersonWithoutDoedsmelding() {
        assertFalse(sjekkDoedsmeldingSentForPerson.execute(aPersonWithoutDoedsmelding));
    }
}
