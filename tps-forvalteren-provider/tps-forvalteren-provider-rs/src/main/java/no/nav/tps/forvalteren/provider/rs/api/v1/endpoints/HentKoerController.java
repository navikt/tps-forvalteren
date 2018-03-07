package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.freg.spring.boot.starters.log.exceptions.LogExceptions;
import no.nav.tps.forvalteren.domain.rs.RsXmlMeldingKo;
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

@RestController
@RequestMapping(value = "api/v1")
@ConditionalOnProperty(prefix = "tps.forvalteren", name = "production-mode", havingValue = "false")
public class HentKoerController {

    private static final String REST_SERVICE_NAME = "service";

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Autowired
    private GetEnvironments getEnvironments;

    @PreAuthorize("hasRole('ROLE_ACCESS')")
    @LogExceptions
    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getQueues")})
    @RequestMapping(value = "/queues", method = RequestMethod.GET)
    public List<RsXmlMeldingKo> getQueues() {

        RsXmlMeldingKo ko;
        Set<String> environments = filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws"));
        List<RsXmlMeldingKo> koListe = new ArrayList<>();

        for (String environment : environments){
            ko = new RsXmlMeldingKo();
            ko.setMiljo(environment);
            ko.setKoNavn(checkIfLocalEnvironment(environment.toUpperCase()));
            koListe.add(ko);
        }
        return koListe;
    }

    private String checkIfLocalEnvironment(String env) {

        String queueName;
        switch (env.toUpperCase()) {
            case "U5":
                queueName = "QA.D8_411.TPS_FORESPORSEL_XML_O";
                break;
            case "U6":
                queueName = "QA.D8_411.TPS_FORESPORSEL_XML_O";
                break;
            default:
                queueName = "QA." + env.toUpperCase() + "_411.TPS_FORESPORSEL_XML_O";
                break;
        }
        return queueName;


        //
    }


}
