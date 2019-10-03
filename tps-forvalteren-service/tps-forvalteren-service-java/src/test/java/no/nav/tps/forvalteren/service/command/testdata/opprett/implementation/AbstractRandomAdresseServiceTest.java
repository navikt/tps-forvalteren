package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;
import com.google.common.io.Resources;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.unmarshaller.TpsServiceRutineS051Unmarshaller;

public abstract class AbstractRandomAdresseServiceTest {

    protected static final String GATEADRESSE = "HAUGASKARSVEGEN";
    protected static final Integer HUSNR_MIN = 0001;
    protected static final Integer HUSNR_MAX = 9999;
    protected static final String POSTNR = "6683";
    protected static final String GATEKODE = "01037";
    protected static final String KOMMUNENR = "1571";


    protected TpsServiceRutineS051Unmarshaller unmarshaller = new TpsServiceRutineS051Unmarshaller();
    protected URL tpsResponsUrl = Resources.getResource("serviceRutine/response/tilfeldigGyldigAdresse_statusFlereAdresserFinnes.xml");
    protected List<Person> enPerson;
    protected List<Person> toPersoner;
    protected TpsServiceRoutineResponse tpsServiceRoutineResponse;
    protected HentGyldigeAdresserService hentGyldigeAdresserServiceMock = mock(HentGyldigeAdresserService.class);
    protected RandomAdresseService randomAdresseService = new RandomAdresseService(unmarshaller, hentGyldigeAdresserServiceMock);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() throws IOException {
        enPerson = singletonList(aMalePerson().build());
        toPersoner = newArrayList(aMalePerson().build(), aMalePerson().build());
        tpsServiceRoutineResponse = createServiceRutineTpsResponse(tpsResponsUrl);
        ReflectionTestUtils.setField(randomAdresseService, "hentDatoFraIdentService", mock(HentDatoFraIdentService.class));

        when(hentGyldigeAdresserServiceMock.hentTilfeldigAdresse(eq(1), any(), any())).thenReturn(tpsServiceRoutineResponse);
    }

    protected TpsServiceRoutineResponse createServiceRutineTpsResponse(URL tpsResponsUrl) throws IOException {
        TpsServiceRoutineResponse tpsServiceRoutineResponse = new TpsServiceRoutineResponse();
        tpsServiceRoutineResponse.setXml(Resources.toString(tpsResponsUrl, StandardCharsets.UTF_8));
        return tpsServiceRoutineResponse;
    }
}
