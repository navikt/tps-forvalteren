package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;

import java.util.List;

@Getter
@Setter
public class TpsSkdMeldingDefinition extends TpsMeldingDefinition {

    private String name;

    private TpsRequestConfig config;

    private List<ServiceRutineAuthorisationStrategy> requiredSecurityServiceStrategies;
}
