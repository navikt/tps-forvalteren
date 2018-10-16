package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.response.AvspillingResponse;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.response.StatusPaaAvspiltSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;

@Service
public class SendSkdMeldingerOgLeggTilResponslisteService {

    @Autowired
    private SendEnSkdMelding sendEnSkdMelding;

    public void sendSkdMeldingAndAddResponseToList(AvspillingResponse avspillingResponse, String skdmelding, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, String env) {
        String status = sendEnSkdMelding.sendSkdMelding(skdmelding, skdRequestMeldingDefinition, env);
        avspillingResponse.incrementAntallSendte();
        if (!"00".equals(status)) {
            rapporterFeiletMelding(status, avspillingResponse);
        }
    }

    private void rapporterFeiletMelding(String status, AvspillingResponse avspillingResponse) {
        StatusPaaAvspiltSkdMelding respons = StatusPaaAvspiltSkdMelding.builder()
                .status(status)
                .build();
        avspillingResponse.addStatusFraFeilendeMeldinger(respons);
        avspillingResponse.incrementAntallFeilet();
    }
}
