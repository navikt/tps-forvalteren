package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsDoedsmeldingRequest;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsFoedselsmeldingRequest;
import no.nav.tps.forvalteren.service.command.dodsmeldinger.SendTpsDoedsmeldingService;
import no.nav.tps.forvalteren.service.command.foedselsmelding.SendTpsFoedselsmeldingService;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.SendSkdMeldingTilTpsResponse;

@RestController
@RequestMapping(value = "api/v1/tpsmelding")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production.mode", havingValue = "false")
public class TpsMeldingController {

    private static final String REST_SERVICE_NAME = "tpsmelding";

    @Autowired
    private SendTpsFoedselsmeldingService sendTpsFoedselsmeldingService;

    @Autowired
    private SendTpsDoedsmeldingService sendTpsDoedsmeldingService;

    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "sendFoedselsmelding") })
    @RequestMapping(value = "/foedselsmelding", method = RequestMethod.POST)
    public SendSkdMeldingTilTpsResponse sendFoedselsmelding(@RequestBody RsTpsFoedselsmeldingRequest tpsFoedselsmeldingRequest) {

        return sendTpsFoedselsmeldingService.sendFoedselsmelding(tpsFoedselsmeldingRequest);
    }

    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "sendDoedsmelding") })
    @RequestMapping(value = "/doedsmelding", method = RequestMethod.POST)
    public SendSkdMeldingTilTpsResponse sendDoedsmelding(@RequestBody RsTpsDoedsmeldingRequest tpsDoedsmeldinpsRequest) {

        return sendTpsDoedsmeldingService.sendDoedsmelding(tpsDoedsmeldinpsRequest);
    }
}