package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.attributter.finngyldigeadresser.Typesok;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class HentGyldigeAdresserServiceTest {
    private final String KOMMUNE_NR = "knret";
    private final int ANTALL = 4;
    private final String POST_NR = "0123";
    ArgumentCaptor<TpsFinnGyldigeAdresserRequest> captor = ArgumentCaptor.forClass(TpsFinnGyldigeAdresserRequest.class);
    private User user;
    @Mock
    private UserContextHolder userContextHolder;
    @Mock
    private TpsRequestSender tpsRequestSender;
    @InjectMocks
    private HentGyldigeAdresserService hentGyldigeAdresserService;
    
    /**
     * Test at tpsRequest opprettes med input-verdiene til "HentTilfeldigAdresse" satt i tpsRequest, og at typesok=T.
     * Test at tpsRequestSender.sendTpsRequest kalles med den requesten.
     */
    @Before
    public void setup() {
        user = new User("name", "username");
        when(userContextHolder.getUser()).thenReturn(user);
        when(tpsRequestSender.sendTpsRequest(any(), any(), anyLong())).thenReturn(new TpsServiceRoutineResponse("<xml>", new Object()));
    }
    
    @Test
    public void shouldHentTilfeldigAdresse() {
        hentGyldigeAdresserService.hentTilfeldigAdresse(ANTALL, KOMMUNE_NR, POST_NR);
        
        Mockito.verify(tpsRequestSender).sendTpsRequest(captor.capture(), any(),anyLong());
        TpsFinnGyldigeAdresserRequest actualRequest = captor.getValue();
        
        assertEquals(new Integer(ANTALL), actualRequest.getMaxRetur());
        assertEquals(KOMMUNE_NR, actualRequest.getKommuneNrsok());
        assertEquals(POST_NR, actualRequest.getPostNrsok());
        assertEquals(Typesok.T, actualRequest.getTypesok());
        assertEquals("A", actualRequest.getAksjonsKode());
        assertEquals("0", actualRequest.getAksjonsKode2());
        assertEquals("FS03-ADRSNAVN-ADRSDATA-O", actualRequest.getServiceRutinenavn());
    }
    
    /**
     * Test at metoden setter serviceRutineNavn og aksjonskode på requesten, og at metoden sende requesten til TPS med fullstendig request.
     */
    @Test
    public void shouldFinnGyldigAdresse() {
        hentGyldigeAdresserService.finnGyldigAdresse(TpsFinnGyldigeAdresserRequest.builder().typesok(Typesok.F).maxRetur(ANTALL).build());
        
        verify(tpsRequestSender).sendTpsRequest(captor.capture(), any(), anyLong());
        TpsFinnGyldigeAdresserRequest actualRequest = captor.getValue();
        
        assertEquals(new Integer(ANTALL), actualRequest.getMaxRetur());
        assertEquals(Typesok.F, actualRequest.getTypesok());
        assertEquals(null, actualRequest.getAdresseNavnsok());
        assertEquals("A", actualRequest.getAksjonsKode());
        assertEquals("0", actualRequest.getAksjonsKode2());
        assertEquals("FS03-ADRSNAVN-ADRSDATA-O", actualRequest.getServiceRutinenavn());
        
    }
}