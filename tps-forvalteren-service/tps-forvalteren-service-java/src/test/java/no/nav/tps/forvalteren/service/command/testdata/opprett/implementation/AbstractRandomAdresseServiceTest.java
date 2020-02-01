package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static java.util.Collections.singletonList;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserResponse;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.HentGyldigeAdresserService;

public abstract class AbstractRandomAdresseServiceTest {

    protected static final String GATEADRESSE = "HAUGASKARSVEGEN";
    protected static final Integer HUSNR_MIN = 0001;
    protected static final Integer HUSNR_MAX = 9999;
    protected static final String POSTNR = "6683";
    protected static final String GATEKODE = "01037";
    protected static final String KOMMUNENR = "1571";

    protected URL tpsResponsUrl = Resources.getResource("serviceRutine/response/tilfeldigGyldigAdresse_statusFlereAdresserFinnes.xml");
    protected List<Person> enPerson;
    protected List<Person> toPersoner;
    protected TpsFinnGyldigeAdresserResponse tpsServiceRoutineResponse;
    protected HentGyldigeAdresserService hentGyldigeAdresserServiceMock = mock(HentGyldigeAdresserService.class);
    protected RandomAdresseService randomAdresseService = new RandomAdresseService(hentGyldigeAdresserServiceMock);

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

    protected TpsFinnGyldigeAdresserResponse createServiceRutineTpsResponse(URL tpsResponsUrl) throws IOException {
        return TpsFinnGyldigeAdresserResponse.builder()
                .xml(Resources.toString(tpsResponsUrl, StandardCharsets.UTF_8))
                .response(TpsFinnGyldigeAdresserResponse.Response.builder()
                        .status(ResponseStatus.builder()
                                .kode("00").build())
                        .data1(TpsFinnGyldigeAdresserResponse.DataContainer.builder()
                                .adrData(singletonList(TpsFinnGyldigeAdresserResponse.Adressedata.builder()
                                        .adrnavn(GATEADRESSE)
                                        .husnrfra(HUSNR_MIN.toString())
                                        .husnrtil(HUSNR_MAX.toString())
                                        .pnr(POSTNR)
                                        .gkode(GATEKODE)
                                        .knr(KOMMUNENR)
                                        .build()))
                                .build()).build())
                .build();
    }
}
