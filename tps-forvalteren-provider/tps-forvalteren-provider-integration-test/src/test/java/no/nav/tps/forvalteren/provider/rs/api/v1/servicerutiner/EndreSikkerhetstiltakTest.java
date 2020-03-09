package no.nav.tps.forvalteren.provider.rs.api.v1.servicerutiner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import no.nav.tps.forvalteren.provider.rs.api.v1.AbstractServiceroutineControllerIntegrationTest;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.TestUserDetails;

public class EndreSikkerhetstiltakTest extends AbstractServiceroutineControllerIntegrationTest {
    
    private static final String serviceRutineNavn = "SikkerhetstiltakEndringsmelding";

    @Override
    protected String getServiceName() {
        return serviceRutineNavn;
    }
    
    /**
     * HVIS en av de påkrevde feltene i servicerutinen mangler, SÅ skal feilmelding kastes.
     * I denne testen mangler feltet "typeSikkerhetsTiltak" for serviceRutineNavn = "endre_sikkerhetstiltak"
     */
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldThrowExceptionBecauseValueIsMissing() throws Exception {
        addRequestParam("offentligIdent", "1234538826");
        addRequestParam("fom", "2014-10-29");
        addRequestParam("tom", "2014-10-30");
        addRequestParam("beskrSikkerhetsTiltak", "Usedvanlig sterk kvinne");
        addRequestParam("environment", "t9");
        
        MvcResult result = mvc.perform(get(getUrl()))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertEquals("F\u00F8lgende p\u00E5krevde felter mangler: [typeSikkerhetsTiltak]", result.getResolvedException().getMessage());
        
        verify(messageQueueConsumer, Mockito.never()).sendMessage(any());
    }
}
