package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;

@RunWith(Parameterized.class)
public class RandomAdresseOnPersonsWithAdresseNrInfoTest extends AbstractRandomAdresseServiceTest {

    @Parameterized.Parameter
    public AdresseNrInfo adresseNrInfo;
    @Parameterized.Parameter(1)
    public String expectedKommunenummer;
    @Parameterized.Parameter(2)
    public String expectedPostnummer;
    
    @Parameterized.Parameters
    public static Collection testparameters() {
        return Arrays.asList(new Object[][] { {null, null, null},
                {new AdresseNrInfo(AdresseNrInfo.AdresseNr.KOMMUNENR,KOMMUNENR), KOMMUNENR,null},
                {new AdresseNrInfo(AdresseNrInfo.AdresseNr.POSTNR,POSTNR), null, POSTNR}
        });
    }

    /**
     * NÅR meldingen kalles med adresseNrInfo, SÅ skal kommunenr eller postnr eller ingen av dem settes som argumenter i servicen hentTilfeldigAdresse.
     */
    @Test
    public void shouldSetCorrectAdresseNr() {
        randomAdresseService.execute(enPerson, adresseNrInfo);
        verify(hentGyldigeAdresserServiceMock).hentTilfeldigAdresse(enPerson.size(), expectedKommunenummer, expectedPostnummer);
    }
}