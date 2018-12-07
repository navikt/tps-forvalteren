package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.response.AvspillingResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;

@RunWith(MockitoJUnitRunner.class)
public class SendSkdMeldingerOgLeggTilResponslisteServiceTest {
    
    private final String miljoe = "u5";
    private final String skdmeldingen = "skdmeldingen";
    @Mock
    private SendEnSkdMelding SendEnSkdMelding;
    
    @InjectMocks
    private SendSkdMeldingerOgLeggTilResponslisteService sendToTps;

    /**
     * Når skdmelding-request feiler i TPS, skal feilmeldingene fra TPS returneres fra service-metoden sendSkdMeldingAndAddResponseToList.
     * Dersom TPS responderer med status OK (TPS returnerer da en string som begynner på 00),
     * så ble skdmeldingene lagret vellykket i TPS. TPSF skal ikke returnere status på vellykkede.
     */
    @Test
    public void shouldReportFailedSkdMessages() {
        String feilmelding = "Feilmelding: skdmeldingen feilet";
        String svarstatus_OK = "00;"; //Må starte med 00
        when(SendEnSkdMelding.sendSkdMelding(any(), any(), any())).thenReturn(svarstatus_OK);
        when(SendEnSkdMelding.sendSkdMelding(eq(skdmeldingen), any(), eq(miljoe))).thenReturn(feilmelding);
        
        AvspillingResponse response = new AvspillingResponse();
        sendToTps.sendSkdMeldingAndAddResponseToList(response, skdmeldingen, new TpsSkdRequestMeldingDefinition(), "annet");
        sendToTps.sendSkdMeldingAndAddResponseToList(response, skdmeldingen, new TpsSkdRequestMeldingDefinition(), miljoe);
        
        assertEquals(2, response.getAntallSendte());
        assertEquals(1, response.getAntallFeilet());
        assertEquals(feilmelding, response.getStatusFraFeilendeMeldinger().get(0).getStatus());
    }
}