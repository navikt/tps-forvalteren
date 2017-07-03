package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameter;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;

import java.util.ArrayList;
import java.util.List;

public class TpsSkdMeldingDefinitionBuilder {
    private String name;
    private TpsRequestConfig requestConfig;
    private List<ServiceRutineAuthorisationStrategy> securitySearchAuthorisationStrategies = new ArrayList<>();

    public TpsSkdMeldingDefinitionBuilder.TpsRequestConfigBuilder config() {
        return new TpsSkdMeldingDefinitionBuilder.TpsRequestConfigBuilder();
    }

    public TpsSkdMeldingDefinitionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TpsSkdMeldingDefinitionBuilder.TpsServiceRoutineSecurityBuilder securityBuilder() {
        return new TpsSkdMeldingDefinitionBuilder.TpsServiceRoutineSecurityBuilder();
    }

    public TpsSkdMeldingDefinition build() {
        TpsSkdMeldingDefinition routine = new TpsSkdMeldingDefinition();
        routine.setName(name);
        routine.setConfig(requestConfig);
        routine.setRequiredSecurityServiceStrategies(securitySearchAuthorisationStrategies);
        return routine;
    }

    public static TpsSkdMeldingDefinitionBuilder aTpsSkdMelding() {
        return new TpsSkdMeldingDefinitionBuilder();
    }

    public class TpsServiceRoutineSecurityBuilder {
        private List<ServiceRutineAuthorisationStrategy> serviceStrategies = new ArrayList<>();

        public TpsSkdMeldingDefinitionBuilder.TpsServiceRoutineSecurityBuilder addRequiredSearchAuthorisationStrategy(ServiceRutineAuthorisationStrategy serviceStrategy){
            this.serviceStrategies.add(serviceStrategy);
            return this;
        }

        public TpsSkdMeldingDefinitionBuilder addSecurity() {
            securitySearchAuthorisationStrategies = this.serviceStrategies;
            return TpsSkdMeldingDefinitionBuilder.this;
        }
    }

    public class TpsRequestConfigBuilder {
        private TpsRequestConfig config;

        TpsRequestConfigBuilder() {
            this.config = new TpsRequestConfig();
        }

        public TpsRequestConfigBuilder requestQueue(String requestQueue){
            config.setRequestQueue(requestQueue);
            return this;
        }

        public TpsSkdMeldingDefinitionBuilder and() {
            TpsSkdMeldingDefinitionBuilder.this.requestConfig = config;
            return TpsSkdMeldingDefinitionBuilder.this;
        }
    }
}
