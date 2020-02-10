package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.rs.skd.RsTpsDoedsmeldingRequest;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsFoedselsmeldingRequest;
import no.nav.tps.forvalteren.service.command.dodsmeldinger.SendTpsDoedsmeldingService;
import no.nav.tps.forvalteren.service.command.foedselsmelding.SendTpsFoedselsmeldingService;

@RunWith(MockitoJUnitRunner.class)
public class TpsMeldingControllerTest {

    @Mock
    private SendTpsFoedselsmeldingService sendTpsFoedselsmeldingService;

    @Mock
    private SendTpsDoedsmeldingService sendTpsDoedsmeldingService;

    @InjectMocks
    private TpsMeldingController tpsMeldingController;

    @Mock
    private RsTpsFoedselsmeldingRequest rsTpsFoedselsmeldingRequest;

    @Mock
    private RsTpsDoedsmeldingRequest rsTpsDoedsmeldingRequest;

    @Test
    public void sendFoedselsmeldingOk() {
        tpsMeldingController.sendFoedselsmelding(rsTpsFoedselsmeldingRequest);

        verify(sendTpsFoedselsmeldingService).sendFoedselsmelding(rsTpsFoedselsmeldingRequest);
    }

    @Test
    public void sendDoedsmeldingOk() {
        tpsMeldingController.sendDoedsmelding(rsTpsDoedsmeldingRequest);

        verify(sendTpsDoedsmeldingService).sendDoedsmelding(rsTpsDoedsmeldingRequest);
    }
}