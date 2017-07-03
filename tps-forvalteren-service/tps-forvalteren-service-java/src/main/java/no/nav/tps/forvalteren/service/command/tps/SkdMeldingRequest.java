package no.nav.tps.forvalteren.service.command.tps;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinition;

import javax.jms.JMSException;

@FunctionalInterface
public interface SkdMeldingRequest {

    String execute(String skdMelding, TpsSkdMeldingDefinition skdMeldingDefinition, String environment) throws JMSException;

}
