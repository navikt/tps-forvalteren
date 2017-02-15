package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.tps.TpsParameter;
import no.nav.tps.vedlikehold.domain.service.tps.config.TpsRequestConfig;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.RequestTransformer;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseTransformer;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
public class TpsServiceRoutineDefinitionBuilder {
    private String name;
    private String internalName;
    private Class<?> javaClass;
    private List<TpsParameter> parameters = new ArrayList<>();
    private List<Transformer> transformers = new ArrayList<>();
    private TpsRequestConfig requestConfig;
    private Set<String> requiredRoles = new HashSet<>();
    private List<AuthorisationStrategy> securitySearchAuthorisationStrategies = new ArrayList<>();

    public TransformerBuilder transformer(){
        return new TransformerBuilder();
    }

    public TpsRequestConfigBuilder config() {
        return new TpsRequestConfigBuilder();
    }

    public TpsServiceRoutineDefinitionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TpsServiceRoutineDefinitionBuilder internalName(String internalName) {
        this.internalName = internalName;
        return this;
    }

    public TpsServiceRoutineDefinitionBuilder javaClass(Class<?> javaClass) {
        this.javaClass = javaClass;
        return this;
    }

    public TpsServiceRoutineParameterBuilder parameter() {
        return new TpsServiceRoutineParameterBuilder();
    }

    public TpsServiceRoutineSecurityBuilder securityBuilder() {
        return new TpsServiceRoutineSecurityBuilder();
    }

    public class TpsServiceRoutineSecurityBuilder {
        private Set<String> access = new HashSet<>();
        private List<AuthorisationStrategy> serviceStrategies = new ArrayList<>();

        public TpsServiceRoutineSecurityBuilder addRequiredRole(String role) {
            this.access.add(role);
            return this;
        }

        public TpsServiceRoutineSecurityBuilder addRequiredSearchAuthorisationStrategy(AuthorisationStrategy serviceStrategy){
            this.serviceStrategies.add(serviceStrategy);
            return this;
        }

        public TpsServiceRoutineDefinitionBuilder addSecurity() {
            requiredRoles = this.access;
            securitySearchAuthorisationStrategies = this.serviceStrategies;
            return TpsServiceRoutineDefinitionBuilder.this;
        }
    }

    public TpsServiceRoutineDefinition build() {
        TpsServiceRoutineDefinition routine = new TpsServiceRoutineDefinition();
        routine.setName(name);
        routine.setInternalName(internalName);
        routine.setJavaClass(javaClass);
        routine.setParameters(parameters);
        routine.setTransformers(transformers);
        routine.setConfig(requestConfig);
        routine.setRequiredRoles(requiredRoles);
        routine.setSecurityServiceStrategies(securitySearchAuthorisationStrategies);
        return routine;
    }

    public static TpsServiceRoutineDefinitionBuilder aTpsServiceRoutine() {
        return new TpsServiceRoutineDefinitionBuilder();
    }

    public class TpsServiceRoutineParameterBuilder {
        private String name;
        private TpsParameterType type;
        private String use;
        private List<String> values = new ArrayList<>();

        public TpsServiceRoutineParameterBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TpsServiceRoutineParameterBuilder type(TpsParameterType type) {
            this.type = type;
            return this;
        }

        public TpsServiceRoutineParameterBuilder required() {
            this.use = "required";
            return this;
        }

        public TpsServiceRoutineParameterBuilder optional() {
            this.use = "optional";
            return this;
        }

        public TpsServiceRoutineParameterBuilder value(String value) {
            this.values.add(value);
            return this;
        }

        public TpsServiceRoutineParameterBuilder values(String... values) {
            this.values.addAll(asList(values));
            return this;
        }

        public TpsServiceRoutineDefinitionBuilder and() {
            TpsParameter param = new TpsParameter();
            param.setName(name);
            param.setType(type);
            param.setUse(use);
            param.setValues(values);
            TpsServiceRoutineDefinitionBuilder.this.parameters.add(param);
            return TpsServiceRoutineDefinitionBuilder.this;
        }
    }

    public class TransformerBuilder {
        private List<Transformer> transformers;

        TransformerBuilder() {
            this.transformers = new ArrayList<>();
        }

        public TransformerBuilder preSend(RequestTransformer transformer) {
            transformers.add(transformer);
            return this;
        }

        public TransformerBuilder postSend(ResponseTransformer transformer) {
            transformers.add(transformer);
            return this;
        }

        public TpsServiceRoutineDefinitionBuilder and() {
            TpsServiceRoutineDefinitionBuilder.this.transformers.addAll(transformers);
            return TpsServiceRoutineDefinitionBuilder.this;
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

        public TpsServiceRoutineDefinitionBuilder and() {
            TpsServiceRoutineDefinitionBuilder.this.requestConfig = config;
            return TpsServiceRoutineDefinitionBuilder.this;
        }
    }

}
