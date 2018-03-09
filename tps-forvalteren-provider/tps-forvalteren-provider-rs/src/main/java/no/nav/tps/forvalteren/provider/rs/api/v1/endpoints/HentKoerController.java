package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.rs.RsXmlMeldingKo;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.forvalteren.provider.rs.config.ProviderConstants.RESTSERVICE;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
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

    private static final String[] QUEUES = {
            "411." + REQUEST_QUEUE_SERVICE_RUTINE_ALIAS,
            "412." + REQUEST_QUEUE_ENDRINGSMELDING_ALIAS
    };

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Autowired
    private GetEnvironments getEnvironments;

    @PreAuthorize("hasRole('ROLE_ACCESS')")
    @LogExceptions
    @Metrics(value = "provider", tags = { @Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getQueues") })
    @RequestMapping(value = "/queues", method = RequestMethod.GET)
    public List<RsXmlMeldingKo> getQueues() {

        RsXmlMeldingKo ko;
        Set<String> environments = filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws"));
        environments = removeDuplicateEnvironments(environments);

        List<RsXmlMeldingKo> koListe = new ArrayList<>();

        for (String environment : environments) {
            for (int i = 0; i < QUEUES.length; i++) {
                ko = new RsXmlMeldingKo();
                ko.setMiljo(environment);
                ko.setKoNavn(setQueueName(environment.toUpperCase(), QUEUES[i]));
                koListe.add(ko);
            }
        }

        return koListe;
    }

    private String setQueueName(String env, String queue) {

        String queueName;
        switch (env.toUpperCase()) {
        case "T0":
        case "Q0":
            queueName = "QA." + env.toUpperCase() + queue;
            break;
        case "U5":
        case "U6":
            queueName = "QA.D8_" + queue;
            break;
        default:
            queueName = "QA." + env.toUpperCase() + "_" + queue;
            break;
        }
        return queueName;

    }

    private Set<String> removeDuplicateEnvironments(Set<String> env) {
        if (env.contains("u5")) {
            env.remove("u5");
        }
        return env;
    }

}
