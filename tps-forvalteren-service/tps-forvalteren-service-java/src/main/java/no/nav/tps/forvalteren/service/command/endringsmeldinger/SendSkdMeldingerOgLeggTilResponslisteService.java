package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static java.util.Objects.nonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.response.AvspillingResponse;
import no.nav.tps.forvalteren.service.command.endringsmeldinger.response.StatusPaaAvspiltSkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.impl.SendEnSkdMelding;

@Service
public class SendSkdMeldingerOgLeggTilResponslisteService {

    private static final String STATUS_OK = "00";

    @Autowired
    private SendEnSkdMelding sendEnSkdMelding;

    public void sendSkdMeldingAndAddResponseToList(
            AvspillingResponse avspillingResponse,
            String skdmelding,
            TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition,
            String env,
            String foedselsnummer,
            String sekvensnummer
    ) {
        String status = sendEnSkdMelding.sendSkdMelding(skdmelding, skdRequestMeldingDefinition, env);
        avspillingResponse.incrementAntallSendte();
        if (status == null || status.length() < 2 || !STATUS_OK.equals(status.substring(0, 2))) {
            rapporterFeiletMelding(status, avspillingResponse, foedselsnummer, sekvensnummer);
        }
    }

    private void rapporterFeiletMelding(
            String status,
            AvspillingResponse avspillingResponse,
            String foedselsnummer,
            String sekvensnummer
    ) {
        StatusPaaAvspiltSkdMelding respons = StatusPaaAvspiltSkdMelding.builder()
                .status(status)
                .foedselsnummer(foedselsnummer)
                .sekvensnummer(nonNull(sekvensnummer) ? Long.parseLong(sekvensnummer) : null)
                .build();
        avspillingResponse.addStatusFraFeilendeMeldinger(respons);
        avspillingResponse.incrementAntallFeilet();
    }
}
