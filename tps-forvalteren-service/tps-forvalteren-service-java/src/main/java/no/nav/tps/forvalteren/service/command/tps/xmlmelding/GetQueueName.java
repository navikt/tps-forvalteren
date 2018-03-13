package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import org.springframework.stereotype.Service;

@Service
public class GetQueueName {

    public String execute(String env, String queue) {

        String queueName;
        if ( env.toUpperCase().contains("U")) {
            queueName = "QA.D8_" + queue;
        } else {
            queueName = "QA." + env.toUpperCase() + "_" + queue;
        }

        return queueName;
    }

}
