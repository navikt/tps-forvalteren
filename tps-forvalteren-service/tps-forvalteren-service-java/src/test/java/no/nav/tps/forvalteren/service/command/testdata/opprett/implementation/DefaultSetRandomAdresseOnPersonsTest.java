package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.google.common.io.Resources;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.unmarshaller.TpsServiceRutineS051Unmarshaller;

public class DefaultSetRandomAdresseOnPersonsTest {
    
    private static final String GATEADRESSE = "HAUGASKARSVEGEN";
    private static final Integer HUSNR_MIN = 0001;
    private static final Integer HUSNR_MAX = 9999;
    private static final String POSTNR = "6683";
    private static final String GATEKODE = "01037";
    private static final String KOMMUNENR = "1571";
    TpsServiceRutineS051Unmarshaller unmarshaller = new TpsServiceRutineS051Unmarshaller();
    URL tpsResponsUrl = Resources.getResource("serviceRutine/response/tilfeldigGyldigAdresse.xml");
    TpsServiceRoutineResponse tpsServiceRoutineResponse;
    private HentGyldigeAdresserService hentGyldigeAdresserService = mock(HentGyldigeAdresserService.class);
    private DefaultSetRandomAdresseOnPersons defaultSetRandomAdresseOnPersons = new DefaultSetRandomAdresseOnPersons(unmarshaller, hentGyldigeAdresserService);
    private List<Person> persons = Arrays.asList(aMalePerson().build());
    
    private TpsServiceRoutineResponse createServiceRutineTpsResponse() throws IOException {
        TpsServiceRoutineResponse tpsServiceRoutineResponse = new TpsServiceRoutineResponse();
        tpsServiceRoutineResponse.setXml(Resources.toString(tpsResponsUrl, StandardCharsets.UTF_8));
        return tpsServiceRoutineResponse;
    }
    
    @Before
    public void setupMock() throws IOException {
        tpsServiceRoutineResponse = createServiceRutineTpsResponse();
        when(hentGyldigeAdresserService.hentTilfeldigAdresse(eq(1), any(), any())).thenReturn(tpsServiceRoutineResponse);
    }
    
    @Test
    public void checkAdresse() {
        defaultSetRandomAdresseOnPersons.execute(persons);
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