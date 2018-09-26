package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.xjc.ctg.domain.s018.BoAdresseType;
import no.nav.tps.xjc.ctg.domain.s018.LMatrAdrType;
import no.nav.tps.xjc.ctg.domain.s018.LOffAdrType;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;

@RunWith(MockitoJUnitRunner.class)
public class PersonAdresseServiceTest {

    private final static String IDENT = "12345678901";
    private final static LocalDateTime BODATO = LocalDateTime.of(2000, 11, 15, 0, 0);
    private final static String MILJOE = "U5";

    @InjectMocks
    private PersonAdresseService personAdresseService;

    @Mock
    private PersonhistorikkService personhistorikkService;

    @Test
    public void hentTomAdresse() throws Exception {

        when(personhistorikkService.hentPersonhistorikk(IDENT, BODATO, MILJOE)).thenReturn(new S018PersonType());

        Gateadresse adresse = (Gateadresse) personAdresseService.hentBoadresseForDato(IDENT, BODATO, MILJOE);

        verify(personhistorikkService).hentPersonhistorikk(IDENT, BODATO, MILJOE);
        assertThat(adresse, is(nullValue()));
    }

    @Test
    public void hentGateBoadresseForDato() throws Exception {

        S018PersonType gateadresse = createGateadresse();
        when(personhistorikkService.hentPersonhistorikk(IDENT, BODATO, MILJOE)).thenReturn(gateadresse);

        Gateadresse adresse = (Gateadresse) personAdresseService.hentBoadresseForDato(IDENT, BODATO, MILJOE);

        verify(personhistorikkService).hentPersonhistorikk(IDENT, BODATO, MILJOE);

        assertThat(adresse instanceof Gateadresse, is(true));
        assertThat(adresse.getGatekode(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getOffAdresse().getGatekode())));
        assertThat(adresse.getAdresse(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getOffAdresse().getGateNavn())));
        assertThat(adresse.getHusnummer(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getOffAdresse().getHusnr() +
                gateadresse.getBostedsAdresse().get(0).getOffAdresse().getBokstav())));

        assertThat(adresse.getFlyttedato(), is(equalTo(BODATO)));
        assertThat(adresse.getKommunenr(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getKommunenr())));
        assertThat(adresse.getPostnr(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getPostnr())));
    }

    @Test
    public void hentMatrikkelBoadresseForDato() throws Exception {

        S018PersonType gateadresse = createMatrikkeladresse();
        when(personhistorikkService.hentPersonhistorikk(IDENT, BODATO, MILJOE)).thenReturn(gateadresse);

        Matrikkeladresse adresse = (Matrikkeladresse) personAdresseService.hentBoadresseForDato(IDENT, BODATO, MILJOE);

        verify(personhistorikkService).hentPersonhistorikk(IDENT, BODATO, MILJOE);

        assertThat(adresse instanceof Matrikkeladresse, is(true));
        assertThat(adresse.getMellomnavn(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getMatrAdresse().getMellomAdresse())));
        assertThat(adresse.getGardsnr(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getMatrAdresse().getGardsnr())));
        assertThat(adresse.getBruksnr(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getMatrAdresse().getBruksnr())));
        assertThat(adresse.getFestenr(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getMatrAdresse().getFestenr())));
        assertThat(adresse.getUndernr(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getMatrAdresse().getUndernr())));

        assertThat(adresse.getFlyttedato(), is(equalTo(BODATO)));
        assertThat(adresse.getKommunenr(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getKommunenr())));
        assertThat(adresse.getPostnr(), is(equalTo(gateadresse.getBostedsAdresse().get(0).getPostnr())));
    }

    private S018PersonType createGateadresse() {

        S018PersonType personType = new S018PersonType();
        BoAdresseType boAdresse = new BoAdresseType();
        boAdresse.setAdresseType("OFFA");
        LOffAdrType gateadresse = new LOffAdrType();
        boAdresse.setOffAdresse(gateadresse);
        gateadresse.setGatekode("12345");
        gateadresse.setGateNavn("Heimsilgata");
        gateadresse.setHusnr("11");
        gateadresse.setBokstav("A");
        personType.getBostedsAdresse().add(addCommonElements(boAdresse));

        return personType;
    }

    private S018PersonType createMatrikkeladresse() {

        S018PersonType personType = new S018PersonType();
        BoAdresseType boAdresse = new BoAdresseType();
        boAdresse.setAdresseType("MATR");
        LMatrAdrType matradresse = new LMatrAdrType();
        boAdresse.setMatrAdresse(matradresse);
        matradresse.setMellomAdresse("Tormodsgaard");
        matradresse.setGardsnr("369");
        matradresse.setBruksnr("987");
        matradresse.setFestenr("456");
        matradresse.setUndernr("123");
        personType.getBostedsAdresse().add(addCommonElements(boAdresse));

        return personType;
    }

    private BoAdresseType addCommonElements(BoAdresseType boadresse) {

        boadresse.setKommunenr("0220");
        boadresse.setPostnr("6820");
        boadresse.setDatoFom(ConvertDateToString.yyyysMMsdd(BODATO));

        return boadresse;
    }
}