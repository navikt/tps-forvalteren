package no.nav.tps.forvalteren.service.command.tps;

import javax.jms.JMSException;

public interface SkdStartAjourhold {

    void execute(String environment) throws JMSException;

}
