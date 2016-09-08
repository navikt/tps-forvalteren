package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
public class TpsServiceRoutineBuilder {
    private String name;
    private String internalName;
    private List<TpsServiceRoutineParameter> parameters = new ArrayList();

    public TpsServiceRoutineBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TpsServiceRoutineBuilder internalName(String internalName) {
        this.internalName = internalName;
        return this;
    }

    public TpsServiceRoutineParameterBuilder parameter() {
        return new TpsServiceRoutineParameterBuilder();
    }

    public class TpsServiceRoutineParameterBuilder {
        private String name;
        private TpsServiceRoutineParameter.Type type;
        private String use;
        private List<String> values = new ArrayList<>();

        public TpsServiceRoutineParameterBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TpsServiceRoutineParameterBuilder type(TpsServiceRoutineParameter.Type type) {
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
            TpsServiceRoutineParameter param = new TpsServiceRoutineParameter();
            param.setName(name);
            param.setType(type);
            param.setUse(use);
            param.setValues(values);
            TpsServiceRoutineBuilder.this.parameters.add(param);
            return TpsServiceRoutineBuilder.this;
        }
    }

    public TpsServiceRoutine build() {
        TpsServiceRoutine routine = new TpsServiceRoutine();
        routine.setName(name);
        routine.setInternalName(internalName);
        routine.setParameters(parameters);
        return routine;
    }

    public static TpsServiceRoutineBuilder aTpsServiceRoutine() {
        return new TpsServiceRoutineBuilder();
    }
}
