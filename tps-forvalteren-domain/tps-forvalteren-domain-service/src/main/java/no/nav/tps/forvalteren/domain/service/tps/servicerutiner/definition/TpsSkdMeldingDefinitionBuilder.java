package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;

import java.util.ArrayList;
import java.util.List;

public class TpsSkdMeldingDefinitionBuilder {
    private String name;
    private TpsRequestConfig requestConfig;
    private SkdParametersCreator skdParametersCreator;
    private List<ServiceRutineAuthorisationStrategy> securitySearchAuthorisationStrategies = new ArrayList<>();

    public TpsSkdMeldingDefinitionBuilder.TpsRequestConfigBuilder config() {
        return new TpsSkdMeldingDefinitionBuilder.TpsRequestConfigBuilder();
    }

    public SkdParametersBuilder skdParameters(){
        return new TpsSkdMeldingDefinitionBuilder.SkdParametersBuilder();
    }

    public TpsSkdMeldingDefinitionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TpsSkdMeldingDefinitionBuilder.TpsServiceRoutineSecurityBuilder securityBuilder() {
        return new TpsSkdMeldingDefinitionBuilder.TpsServiceRoutineSecurityBuilder();
    }

    public TpsSkdRequestMeldingDefinition build() {
        TpsSkdRequestMeldingDefinition routine = new TpsSkdRequestMeldingDefinition();
        routine.setName(name);
        routine.setConfig(requestConfig);
        routine.setRequiredSecurityServiceStrategies(securitySearchAuthorisationStrategies);
        routine.setSkdParametersCreator(skdParametersCreator);
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

    public class SkdParametersBuilder {
        private SkdParametersCreator innerSkdParametersCreator;

        public SkdParametersBuilder addSkdParametersCreator(SkdParametersCreator skdParametersCreator){
            this.innerSkdParametersCreator = skdParametersCreator;
            return this;
        }

        public SkdParametersBuilder addParameterCreator() {
             skdParametersCreator = this.innerSkdParametersCreator;
             return this;
        }

        public TpsSkdMeldingDefinitionBuilder and() {
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
