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
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import com.google.common.io.Resources;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.s051.unmarshaller.TpsServiceRutineS051Unmarshaller;

public abstract class AbstractSetRandomAdresseOnPersonsTest {
    
    protected static final String GATEADRESSE = "HAUGASKARSVEGEN";
    protected static final Integer HUSNR_MIN = 0001;
    protected static final Integer HUSNR_MAX = 9999;
    protected static final String POSTNR = "6683";
    protected static final String GATEKODE = "01037";
    protected static final String KOMMUNENR = "1571";
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    TpsServiceRutineS051Unmarshaller unmarshaller = new TpsServiceRutineS051Unmarshaller();
    URL tpsResponsUrl = Resources.getResource("serviceRutine/response/tilfeldigGyldigAdresse_statusFlereAdresserFinnes.xml");
    List<Person> enPerson;
    List<Person> toPersoner;
    TpsServiceRoutineResponse tpsServiceRoutineResponse;
    HentGyldigeAdresserService hentGyldigeAdresserServiceMock = mock(HentGyldigeAdresserService.class);
    SetRandomAdresseOnPersons setRandomAdresseOnPersons = new SetRandomAdresseOnPersons(unmarshaller, hentGyldigeAdresserServiceMock);
    
    TpsServiceRoutineResponse createServiceRutineTpsResponse(URL tpsResponsUrl) throws IOException {
        TpsServiceRoutineResponse tpsServiceRoutineResponse = new TpsServiceRoutineResponse();
        tpsServiceRoutineResponse.setXml(Resources.toString(tpsResponsUrl, StandardCharsets.UTF_8));
        return tpsServiceRoutineResponse;
    }
    
    @Before
    public void setup() throws IOException {
        enPerson = Arrays.asList(aMalePerson().build());
        toPersoner = Arrays.asList(aMalePerson().build(), aMalePerson().build());
        tpsServiceRoutineResponse = createServiceRutineTpsResponse(tpsResponsUrl);
        when(hentGyldigeAdresserServiceMock.hentTilfeldigAdresse(eq(1), any(), any())).thenReturn(tpsServiceRoutineResponse);
    }
    
}
