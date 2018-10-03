package no.nav.tps.forvalteren.service.command.innvandring;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdent;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddInndringsdatoOgLandTilPersonerServiceTest {

    private Person emptyPerson1 = new Person();
    private Person emptyPerson2 = new Person();
    private List<Person> personListe = new ArrayList<>();

    @Mock
    private LandkodeEncoder landkodeEncoder;

    @Mock
    private HentDatoFraIdent hentDatoFraIdent;

    @InjectMocks
    private AddInndringsdatoOgLandTilPersonerService addInndringsdatoOgLandTilPersonerService;

    @Before
    public void setup(){
        when(landkodeEncoder.getRandomLandTla()).thenReturn("random");
    }

    @Test
    public void execute() {
        emptyPerson1.setIdent("01011012345");
        emptyPerson2.setIdent("05050512345");
        personListe.add(emptyPerson1);
        personListe.add(emptyPerson2);

        LocalDateTime time1 = LocalDateTime.of(2010, 1,1, 0,0);
        LocalDateTime time2 = LocalDateTime.of(2005, 5,5, 0,0);
        when(hentDatoFraIdent.extract("01011012345")).thenReturn(time1);
        when(hentDatoFraIdent.extract("05050512345")).thenReturn(time2);

        addInndringsdatoOgLandTilPersonerService.execute(personListe);

        assertThat(personListe.get(0).getInnvandretFraLand(), is("random"));
        assertThat(personListe.get(1).getInnvandretFraLand(), is("random"));
        assertThat(personListe.get(0).getInnvandretFraLandFlyttedato(), is(time1));
        assertThat(personListe.get(1).getInnvandretFraLandFlyttedato(), is(time2));
    }
}