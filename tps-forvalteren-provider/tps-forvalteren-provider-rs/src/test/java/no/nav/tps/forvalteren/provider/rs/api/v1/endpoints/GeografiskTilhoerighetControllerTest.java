package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@RunWith(MockitoJUnitRunner.class)
public class GeografiskTilhoerighetControllerTest {

    private static final String KJERNEINFO_SERVICE_ROUTINE = "FS03-FDNUMMER-KERNINFO-O";
    private static final String ADRESSEHISTORIKK_SERVICE_ROUTINE = "FS03-FDNUMMER-ADRHISTO-O";
    private static final String ADRESSELINJEHISTORIKK_SERVICE_ROUTINE = "FS03-FDNUMMER-ADLIHIST-O";
    private static final String SOAIHISTORIKK_SERVICE_ROUTINE = "FS03-FDNUMMER-SOAIHIST-O";

    @InjectMocks
    private GeografiskTilhoerighetController gtController;

    @Mock
    private Map tpsParameters;

    @Mock
    private TpsServiceRoutineService tpsServiceRoutineService;

    private ArgumentCaptor<String> argumentCaptor;

    @Before
    public void setup() {
        argumentCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    public void getKerninfo() throws Exception {

        gtController.getKerninfo(tpsParameters);

        verify(tpsServiceRoutineService).execute(argumentCaptor.capture(), eq(tpsParameters), anyBoolean());
        assertThat(argumentCaptor.getValue(), is(equalTo(KJERNEINFO_SERVICE_ROUTINE)));
    }

    @Test
    public void getAdrhist() throws Exception {

        gtController.getAdrhist(tpsParameters);

        verify(tpsServiceRoutineService).execute(argumentCaptor.capture(), eq(tpsParameters), anyBoolean());
        assertThat(argumentCaptor.getValue(), is(equalTo(ADRESSEHISTORIKK_SERVICE_ROUTINE)));
    }

    @Test
    public void getAdrlinjhist() throws Exception {

        gtController.getAdrlinjhist(tpsParameters);

        verify(tpsServiceRoutineService).execute(argumentCaptor.capture(), eq(tpsParameters), anyBoolean());
        assertThat(argumentCaptor.getValue(), is(equalTo(ADRESSELINJEHISTORIKK_SERVICE_ROUTINE)));
    }

    @Test
    public void getSoaihist() throws Exception {

        gtController.getSoaihist(tpsParameters);

        verify(tpsServiceRoutineService).execute(argumentCaptor.capture(), eq(tpsParameters), anyBoolean());
        assertThat(argumentCaptor.getValue(), is(equalTo(SOAIHISTORIKK_SERVICE_ROUTINE)));
    }
}