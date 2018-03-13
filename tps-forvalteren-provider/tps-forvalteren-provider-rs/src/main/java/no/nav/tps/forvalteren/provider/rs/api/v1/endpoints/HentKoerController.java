package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.List;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.rs.RsXmlMeldingKo;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.GetQueuesFromEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ROLE_ACCESS')")
@RestController
@RequestMapping(value = "api/v1")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class HentKoerController {

    private static final String REST_SERVICE_NAME = "service";

    @Autowired
    private GetQueuesFromEnvironment getQueuesFromEnvironment;

    @PreAuthorize("hasRole('ROLE_ACCESS')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getQueues") })
    @RequestMapping(value = "/queues", method = RequestMethod.GET)
    public List<RsXmlMeldingKo> getQueues() {
        return getQueuesFromEnvironment.execute();
    }

}
