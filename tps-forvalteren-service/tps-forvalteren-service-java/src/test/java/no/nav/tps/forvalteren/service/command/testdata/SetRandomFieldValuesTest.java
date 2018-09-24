package no.nav.tps.forvalteren.service.command.testdata;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetRandomFieldValues;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@RunWith(MockitoJUnitRunner.class)
public class SetRandomFieldValuesTest {

    @InjectMocks
    private SetRandomFieldValues setRandomFieldValues;

    @Mock
    Person person;

    @Mock
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Test
    public void getRandomDoedsdatoTest() {
        person = new Person();
        person.setIdent("0101200012345");

        LocalDateTime date = LocalDateTime.of(1998, 1, 7, 0, 0);
        when(hentDatoFraIdentService.extract(person.getIdent())).thenReturn(date);
        setRandomFieldValues.execute("doedsdato", person);

        assertThat(person.getDoedsdato(), instanceOf(LocalDateTime.class));
    }

    @Test
    public void getRandomLandKodeTest() {
        person = new Person();

        for (int i = 0; i < 10; i++) {
            setRandomFieldValues.execute("statsborgerskap", person);
            assertTrue(person.getStatsborgerskap().matches("\\d{3}"));
        }
    }

    @Test
    public void getRandomSpesregTest() {
        person = new Person();

        for (int i = 0; i < 10; i++) {
            setRandomFieldValues.execute("spesreg", person);
            assertTrue(person.getSpesreg().matches("\\d{1}"));
        }
    }

    @Test
    public void getRandomSpesregDatoTest() {
        person = new Person();

        LocalDateTime date = LocalDateTime.of(1998, 1, 7, 0, 0);
        when(hentDatoFraIdentService.extract(anyString())).thenReturn(date);

        setRandomFieldValues.execute("spesregDato", person);

        assertThat(person.getSpesregDato(), instanceOf(LocalDateTime.class));
    }

    @Test
    public void getRandomSivilstandTest() {
        person = new Person();

        for (int i = 0; i < 10; i++) {
            setRandomFieldValues.execute("sivilstand", person);
            assertTrue(person.getSivilstand().matches("\\d{1}"));
        }
    }
}
