package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import com.google.common.io.Resources;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;

@RunWith(Parameterized.class)
public class RandomAdresseServiceTest extends AbstractRandomAdresseServiceTest {

    @Parameterized.Parameter
    public String resource;
    @Parameterized.Parameters
    public static Collection testparameters() {
        return Arrays.asList(new Object[][] {
                { "serviceRutine/response/tilfeldigGyldigAdresse_statuskode00.xml"},
                { "serviceRutine/response/tilfeldigGyldigAdresse_statusForMangeTreff.xml"},
                { "serviceRutine/response/tilfeldigGyldigAdresse_statusFlereAdresserFinnes.xml"}
        });
    }
    
    @Before
    public void setupTestdata() throws IOException {
        tpsServiceRoutineResponse = createServiceRutineTpsResponse(Resources.getResource(resource));
        when(hentGyldigeAdresserServiceMock.hentTilfeldigAdresse(eq(2), any(), any())).thenReturn(tpsServiceRoutineResponse);
    }
    
    /**
     * HVIS status.kode = "00" ELLER status.melding = "S051003I" (Flere Adresser finnes) ELLER status.melding = "S051002I" (For mange treff ),
     * SÅ skal det opprettes gateadresse på alle personer, og tjenesten regnes som vellykket.
     */
    @Test
    public void shouldHentTilfeldigAdresse() {
        randomAdresseService.execute(toPersoner, new AdresseNrInfo(AdresseNrInfo.AdresseNr.KOMMUNENR,KOMMUNENR));
        
        assertAdresse((Gateadresse) toPersoner.get(0).getBoadresse());
        assertAdresse((Gateadresse) toPersoner.get(1).getBoadresse());
    }

    @Test
    public void setRandomAdresseUtenKommuneEllerPostnummer() {
        randomAdresseService.execute(toPersoner);

        assertAdresse((Gateadresse) toPersoner.get(0).getBoadresse());
        assertAdresse((Gateadresse) toPersoner.get(1).getBoadresse());
    }
    
    private void assertAdresse(Gateadresse adresse) {
        assertTrue("husnummer max", Integer.parseInt(adresse.getHusnummer()) < HUSNR_MAX);
        assertTrue("husnummer min", Integer.parseInt(adresse.getHusnummer()) > HUSNR_MIN);
        assertThat(adresse.getGatekode(), is(GATEKODE));
        assertThat(adresse.getKommunenr(), is(KOMMUNENR));
        assertThat(adresse.getAdresse(), is(GATEADRESSE));
        assertThat(adresse.getPostnr(), is(POSTNR));
    }
}