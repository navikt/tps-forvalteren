package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import javax.xml.bind.JAXBException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.unmarshaller.TpsServiceRutineS051Unmarshaller;
import no.nav.tps.xjc.ctg.domain.s051.StatusFraTPS;
import no.nav.tps.xjc.ctg.domain.s051.SvarFraTPS;
import no.nav.tps.xjc.ctg.domain.s051.TpsAdresseData;

@RunWith(Parameterized.class)
public class RandomAdresseServiceHandleStatusmeldingTest extends AbstractRandomAdresseServiceTest {
    
    @Parameterized.Parameter
    public String utfyllendeMelding;
    @Parameterized.Parameter(1)
    public String statuskode;
    @Parameterized.Parameter(2)
    public String statusmelding;
    public StatusFraTPS statusFraTPS = new StatusFraTPS();
    
    private TpsServiceRutineS051Unmarshaller unmarshallerMock = mock(TpsServiceRutineS051Unmarshaller.class);
    private RandomAdresseService randomAdresseService_AllMocks = new RandomAdresseService(unmarshallerMock, hentGyldigeAdresserServiceMock);
    
    @Parameterized.Parameters
    public static Collection testparameters() {
        return Arrays.asList(new Object[][] {
                { "Ingen adresser funnet", "04", "S051001I" },
                { "Ugyldig Postnummer", "08", "S051003F" }
        });
    }
    
    @Before
    public void setupTestdata() throws JAXBException {
        statusFraTPS.setReturMelding(statusmelding);
        statusFraTPS.setReturStatus(statuskode);
        statusFraTPS.setUtfyllendeMelding(utfyllendeMelding);
        
        TpsAdresseData tpsAdresseData = new TpsAdresseData();
        tpsAdresseData.setTpsSvar(new SvarFraTPS());
        tpsAdresseData.getTpsSvar().setSvarStatus(statusFraTPS);
        
        when(unmarshallerMock.unmarshal(any())).thenReturn(tpsAdresseData);
    }
    
    /**
     * HVIS status.kode = "08" SÅ skal feilmelding kastes.
     * HVIS status.melding = "S051001I" (Ingen adresser funnet) SÅ skal feilmelding kastes.
     */
    @Test
    public void shouldThrowException() {
        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage(utfyllendeMelding);
        
        randomAdresseService_AllMocks.execute(enPerson, new AdresseNrInfo(AdresseNrInfo.AdresseNr.KOMMUNENR, KOMMUNENR));
    }
}