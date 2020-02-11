package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.XML_REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.XML_REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.RsTpsMeldingKo;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;

@Service
public class GetQueuesFromEnvironment {

    private static final String[] QUEUES = {
            XML_REQUEST_QUEUE_SERVICE_RUTINE_ALIAS,
            XML_REQUEST_QUEUE_ENDRINGSMELDING_ALIAS
    };

    @Autowired
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Autowired
    private GetQueueName getQueueName;

    @Autowired
    private GetEnvironments getEnvironments;

    public List<RsTpsMeldingKo> execute(String appNavn) {

        RsTpsMeldingKo ko;
        Set<String> environments = filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit(appNavn));
        removeDuplicateTestEnvironments(environments);

        List<RsTpsMeldingKo> koListe = new ArrayList<>();

        for (String environment : environments) {
            for (int i = 0; i < QUEUES.length; i++) {
                ko = new RsTpsMeldingKo();
                ko.setMiljo(environment);
                ko.setKoNavn(getQueueName.execute(environment.toUpperCase(), QUEUES[i]));
                koListe.add(ko);
            }
        }

        return koListe;
    }

    private Set<String> removeDuplicateTestEnvironments(Set<String> env) {
        if (env.contains("u5")) {
            env.remove("u5");
        }
        return env;
    }

}
