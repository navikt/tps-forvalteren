package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.domain.rs.AdresseNrInfo.AdresseNr.kommuneNr;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;

public class SetRandomAdresseOnPersonsTest extends AbstractSetRandomAdresseOnPersonsTest{
    
    @Test
    public void shouldHentTilfeldigAdresse() {
        setRandomAdresseOnPersons.execute(persons, new AdresseNrInfo(kommuneNr,KOMMUNENR));
        Gateadresse adresse = (Gateadresse) persons.get(0).getBoadresse();
        assertTrue("husnummer max", Integer.parseInt(adresse.getHusnummer()) < HUSNR_MAX);
        assertTrue("husnummer min", Integer.parseInt(adresse.getHusnummer()) > HUSNR_MIN);
        assertThat(adresse.getGatekode(), is(GATEKODE));
        assertThat(adresse.getKommunenr(), is(KOMMUNENR));
        assertThat(adresse.getAdresse(), is(GATEADRESSE));
        assertThat(adresse.getPostnr(), is(POSTNR));
        //        assertThat(adresse.getFlyttedato(), is());
        
    }
}