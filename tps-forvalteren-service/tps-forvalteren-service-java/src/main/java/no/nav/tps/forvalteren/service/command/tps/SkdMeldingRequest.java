package no.nav.tps.forvalteren.service.command.tps;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;

import javax.jms.JMSException;

public interface SkdMeldingRequest {

    String execute(String skdMelding, TpsSkdMeldingDefinition skdMeldingDefinition, TpsRequestContext context) throws JMSException;

}
