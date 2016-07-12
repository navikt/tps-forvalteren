package no.nav.tps.vedlikehold.provider.web.selftest;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.DefaultEgenAnsattConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Component
public class TpswsSelftest extends SubSystemSelftest {

    @Autowired
    private DefaultEgenAnsattConsumer egenAnsattConsumer;

    @Override
    protected String getSubSystemName() {
        return "TPSWS";
    }

    @Override
    protected boolean performCheck() {
        pingEgenAnsattConsumer.ping();
        return true;
    }
}
