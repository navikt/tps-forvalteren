package no.nav.tps.forvalteren.service.command.tps;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;

import javax.jms.JMSException;

@FunctionalInterface
public interface SkdMeldingRequest {

    void execute(String skdMelding, TpsSkdRequestMeldingDefinition skdMeldingDefinition, String environment) throws JMSException;

}
