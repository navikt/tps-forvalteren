package no.nav.tps.forvalteren.provider.rs.api.v1.servicerutiner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.provider.rs.api.v1.AbstractServiceControllerIntegrationTest;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.TestUserDetails;

public class EndreSikkerhetstiltakTest extends AbstractServiceControllerIntegrationTest{
    
    private static final String serviceRutineNavn = "endre_sikkerhetstiltak";

    @Override
    protected String getServiceName() {
        return serviceRutineNavn;
    }
    
    /**
     * HVIS alle påkrevde verdier i request resolveren er fylt inn (i queryParam) SÅ skal xml opprettes og legges på kø til TPS.
     * @throws Exception
     */
    @Test
    @WithUserDetails(TestUserDetails.USERNAME)
    public void shouldsendeServiceRutineMeldingTilTps() throws Exception {

        setResponseQueueMessage(getResourceFileContent("testdata/servicerutiner/endre_sikkerhetstiltak_tps_response.xml"));

        addRequestParam("offentligIdent", "1234538826");
        addRequestParam("typeSikkerhetsTiltak", "USKK");
        addRequestParam("fom", "2014-10-29");
        addRequestParam("tom", "2014-10-30");
        addRequestParam("beskrSikkerhetsTiltak", "Usedvanlig sterk kvinne");
        addRequestParam("environment", "t9");

        MvcResult result = mvc.perform(get(getUrl()))
                .andExpect(status().isOk())
                .andReturn();

        //I Denne filen er det gjort om fra "test_username" til "username" for å få den til å virke.
        Mockito.verify(messageQueueConsumer).sendMessage(removeNewLineAndTab(getResourceFileContent("testdata/servicerutiner/endre_sikkerhetstiltak_tps_request.xml")));
        
        TpsServiceRoutineResponse response = convertMvcResultToObject(result, TpsServiceRoutineResponse.class);
        String expectedResponse = removeNewLineAndTab(getResourceFileContent("testdata/servicerutiner/endre_sikkerhetstiltak_tps_response.xml"));
        assertEquals(expectedResponse, removeNewLineAndTab(response.getXml()));
        
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
    
        assertEquals("Følgende påkrevde felter mangler:[typeSikkerhetsTiltak]", result.getResolvedException().getMessage());
        
        Mockito.verify(messageQueueConsumer, Mockito.never()).sendMessage(any());
    
    }
}
