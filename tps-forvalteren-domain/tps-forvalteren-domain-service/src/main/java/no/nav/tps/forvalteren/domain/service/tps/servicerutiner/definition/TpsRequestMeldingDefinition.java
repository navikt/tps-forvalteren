package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;
import org.codehaus.jackson.annotate.JsonIgnore;

@Getter
@Setter
public class TpsRequestMeldingDefinition extends DBRequestMeldingDefinition {

    private String name;

    @JsonIgnore
    private TpsRequestConfig config;
}
