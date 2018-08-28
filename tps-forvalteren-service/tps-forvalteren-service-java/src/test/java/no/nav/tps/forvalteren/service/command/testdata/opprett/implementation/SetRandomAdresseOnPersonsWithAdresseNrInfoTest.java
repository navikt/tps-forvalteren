package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;

@RunWith(Parameterized.class)
public class SetRandomAdresseOnPersonsWithAdresseNrInfoTest extends AbstractSetRandomAdresseOnPersonsTest {
    
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
        setRandomAdresseOnPersons.execute(enPerson, adresseNrInfo);
        Mockito.verify(hentGyldigeAdresserServiceMock).hentTilfeldigAdresse(enPerson.size(), expectedKommunenummer, expectedPostnummer);
    }
}