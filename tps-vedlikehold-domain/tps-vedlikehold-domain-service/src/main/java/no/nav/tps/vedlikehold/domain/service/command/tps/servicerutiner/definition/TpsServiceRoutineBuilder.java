package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
public class TpsServiceRoutineBuilder {
    private String name;
    private String internalName;
    private Class<? extends TpsRequestServiceRoutine> javaClass;
    private List<TpsParameter> parameters = new ArrayList();
    private Set<String> requiredRoles = new HashSet<>();
    private List<AuthorisationStrategy> securitySearchAuthorisationStrategies = new ArrayList<>();

    public TpsServiceRoutineBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TpsServiceRoutineBuilder internalName(String internalName) {
        this.internalName = internalName;
        return this;
    }

    public TpsServiceRoutineBuilder javaClass(Class<? extends TpsRequestServiceRoutine> javaClass) {
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

        public TpsServiceRoutineBuilder addSecurity() {
            requiredRoles = this.access;
            securitySearchAuthorisationStrategies = this.serviceStrategies;
            return TpsServiceRoutineBuilder.this;
        }
    }

    public TpsServiceRoutine build() {
        TpsServiceRoutine routine = new TpsServiceRoutine();
        routine.setName(name);
        routine.setInternalName(internalName);
        routine.setJavaClass(javaClass);
        routine.setParameters(parameters);
        routine.setRequiredRoles(requiredRoles);
        routine.setSecurityServiceStrategy(securitySearchAuthorisationStrategies);
        //Kanskje loope igjenom stratgier og deretter hente hvilke param de trenger ved å loope gjennom Param.
        return routine;
    }

    public static TpsServiceRoutineBuilder aTpsServiceRoutine() {
        return new TpsServiceRoutineBuilder();
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

        public TpsServiceRoutineBuilder and() {
            TpsParameter param = new TpsParameter();
            param.setName(name);
            param.setType(type);
            param.setUse(use);
            param.setValues(values);
            TpsServiceRoutineBuilder.this.parameters.add(param);
            return TpsServiceRoutineBuilder.this;
        }
    }
}
