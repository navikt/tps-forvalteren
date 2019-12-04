package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.test.util.ReflectionTestUtils;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.xjc.ctg.domain.s051.StatusFraTPS;
import no.nav.tps.xjc.ctg.domain.s051.SvarFraTPS;
import no.nav.tps.xjc.ctg.domain.s051.TpsAdresseData;

@RunWith(Parameterized.class)
public class RandomAdresseServiceHandleStatusmeldingTest extends AbstractRandomAdresseServiceTest {

    private static final LocalDateTime FODSELSDAG = LocalDateTime.of(1975,1,1,0,0);

    @Parameterized.Parameter
    public String utfyllendeMelding;
    @Parameterized.Parameter(1)
    public String statuskode;
    @Parameterized.Parameter(2)
    public String statusmelding;
    public StatusFraTPS statusFraTPS = new StatusFraTPS();

    private RandomAdresseService randomAdresseService_AllMocks = new RandomAdresseService(hentGyldigeAdresserServiceMock);
    private DummyAdresseService dummyAdresseService = mock(DummyAdresseService.class);
    private HentDatoFraIdentService hentDatoFraIdentService = mock(HentDatoFraIdentService.class);
    
    @Parameterized.Parameters
    public static Collection testparameters() {
        return Arrays.asList(new Object[][] {
                { "Ingen adresser funnet", "04", "S051001I" },
                { "Ugyldig Postnummer", "08", "S051003F" }
        });
    }
    
    @Before
    public void setupTestdata() {
        statusFraTPS.setReturMelding(statusmelding);
        statusFraTPS.setReturStatus(statuskode);
        statusFraTPS.setUtfyllendeMelding(utfyllendeMelding);
        
        TpsAdresseData tpsAdresseData = new TpsAdresseData();
        tpsAdresseData.setTpsSvar(new SvarFraTPS());
        tpsAdresseData.getTpsSvar().setSvarStatus(statusFraTPS);

        ReflectionTestUtils.setField(randomAdresseService_AllMocks, "dummyAdresseService", dummyAdresseService);
        ReflectionTestUtils.setField(randomAdresseService_AllMocks, "hentDatoFraIdentService", hentDatoFraIdentService);

        when(dummyAdresseService.createDummyBoAdresse(null)).thenReturn(Gateadresse.builder().build());

        when(hentDatoFraIdentService.extract(anyString())).thenReturn(FODSELSDAG);
    }
    
    /**
     * HVIS status.kode = "08" SÅ skal feilmelding kastes.
     * HVIS status.melding = "S051001I" (Ingen adresser funnet) SÅ skal feilmelding ikke kastes, men gi soft fallback
     */
    @Test
    public void ingenAdresseFunnet_shouldNotThrowException() {
        
        List<Person> result = randomAdresseService_AllMocks.execute(enPerson, new AdresseNrInfo(AdresseNrInfo.AdresseNr.KOMMUNENR, KOMMUNENR));

        Assert.assertTrue(result.get(0).getBoadresse() instanceof Gateadresse);
    }
}