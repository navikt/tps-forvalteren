package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
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
import com.google.common.io.Resources;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051.unmarshaller.TpsServiceRutineS051Unmarshaller;

public abstract class AbstractSetRandomAdresseOnPersonsTest {
    
    static final String GATEADRESSE = "HAUGASKARSVEGEN";
    static final Integer HUSNR_MIN = 0001;
    static final Integer HUSNR_MAX = 9999;
    static final String POSTNR = "6683";
    static final String GATEKODE = "01037";
    static final String KOMMUNENR = "1571";
    
    TpsServiceRutineS051Unmarshaller unmarshaller = new TpsServiceRutineS051Unmarshaller();
    URL tpsResponsUrl = Resources.getResource("serviceRutine/response/tilfeldigGyldigAdresse.xml");
    List<Person> persons = Arrays.asList(aMalePerson().build());
    
    TpsServiceRoutineResponse tpsServiceRoutineResponse;
    
    HentGyldigeAdresserService hentGyldigeAdresserService = mock(HentGyldigeAdresserService.class);
    
    SetRandomAdresseOnPersons setRandomAdresseOnPersons = new SetRandomAdresseOnPersons(unmarshaller, hentGyldigeAdresserService);
    
    TpsServiceRoutineResponse createServiceRutineTpsResponse() throws IOException {
        TpsServiceRoutineResponse tpsServiceRoutineResponse = new TpsServiceRoutineResponse();
        tpsServiceRoutineResponse.setXml(Resources.toString(tpsResponsUrl, StandardCharsets.UTF_8));
        return tpsServiceRoutineResponse;
    }
    
    @Before
    public void setup() throws IOException {
        tpsServiceRoutineResponse = createServiceRutineTpsResponse();
        when(hentGyldigeAdresserService.hentTilfeldigAdresse(eq(1), any(), any())).thenReturn(tpsServiceRoutineResponse);
    }
    
}
