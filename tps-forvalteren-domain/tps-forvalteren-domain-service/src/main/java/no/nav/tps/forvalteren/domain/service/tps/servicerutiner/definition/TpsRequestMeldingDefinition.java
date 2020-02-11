package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;

@Getter
@Setter
public class TpsRequestMeldingDefinition extends DBRequestMeldingDefinition {

    private String name;

    @JsonIgnore
    private TpsRequestConfig config;

    private SkdParametersCreator skdParametersCreator;
}
