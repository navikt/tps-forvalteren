package no.nav.tps.forvalteren.service.command.tps.xmlmelding;

import org.springframework.stereotype.Service;

@Service
public class GetQueueName {

    public String execute(String env, String queue) {

        String queueName;
        switch (env.toUpperCase()) {
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

}
