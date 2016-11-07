//package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition;
//
//import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameter;
//import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
//import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static java.util.Arrays.asList;
//
///**
// * Created by f148888 on 29.09.2016.
// */
//public class TpsEndringsmeldingBuilder {
//    private String name;
//    private String internalName;
//    private Class<? extends TpsRequestEndringsmelding> javaClass;
//    private List<TpsParameter> parameters = new ArrayList();
//
//    public TpsEndringsmeldingBuilder name(String name) {
//        this.name = name;
//        return this;
//    }
//
//    public TpsEndringsmeldingBuilder internalName(String internalName) {
//        this.internalName = internalName;
//        return this;
//    }
//
//    public TpsEndringsmeldingBuilder javaClass(Class<? extends TpsRequestEndringsmelding> javaClass) {
//        this.javaClass = javaClass;
//        return this;
//    }
//
//    public TpsEndringsmeldingParameterBuilder parameter() {
//        return new TpsEndringsmeldingParameterBuilder();
//    }
//
//    public TpsEndringsmelding build() {
//        TpsEndringsmelding endringsmelding = new TpsEndringsmelding();
//        endringsmelding.setName(name);
//        endringsmelding.setInternalName(internalName);
//        endringsmelding.setJavaClass(javaClass);
//        endringsmelding.setParameters(parameters);
//        return endringsmelding;
//    }
//
//    public static TpsEndringsmeldingBuilder aTpsEndringsmelding() {
//        return new TpsEndringsmeldingBuilder();
//    }
//
//    public class TpsEndringsmeldingParameterBuilder {
//        private String name;
//        private TpsParameterType type;
//        private String use;
//        private List<String> values = new ArrayList<>();
//
//        public TpsEndringsmeldingParameterBuilder name(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public TpsEndringsmeldingParameterBuilder type(TpsParameterType type) {
//            this.type = type;
//            return this;
//        }
//
//        public TpsEndringsmeldingParameterBuilder required() {
//            this.use = "required";
//            return this;
//        }
//
//        public TpsEndringsmeldingParameterBuilder optional() {
//            this.use = "optional";
//            return this;
//        }
//
//        public TpsEndringsmeldingParameterBuilder value(String value) {
//            this.values.add(value);
//            return this;
//        }
//
//        public TpsEndringsmeldingParameterBuilder values(String... values) {
//            this.values.addAll(asList(values));
//            return this;
//        }
//
//        public TpsEndringsmeldingBuilder and() {
//            //TODO TpsParam kanskje utvide til endrings param og added Kilde. Eller kanskje endre type?
//            TpsParameter param = new TpsParameter();
//            param.setName(name);
//            param.setType(type);
//            param.setUse(use);
//            param.setValues(values);
//            TpsEndringsmeldingBuilder.this.parameters.add(param);
//            return TpsEndringsmeldingBuilder.this;
//        }
//    }
//}
