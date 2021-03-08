package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.MidlertidigAdresse;
import no.nav.tps.forvalteren.domain.rs.RsMidlertidigAdresse;

@Component
public class MidlertidigAdresseMappingStrategy implements MappingStrategy {


    @Override
    public void register(MapperFactory factory) {
        factory.classMap(MidlertidigAdresse.MidlertidigGateAdresse.class, RsMidlertidigAdresse.MidlertidigGateAdresse.class)
                .customize(new CustomMapper<MidlertidigAdresse.MidlertidigGateAdresse, RsMidlertidigAdresse.MidlertidigGateAdresse>() {
                    @Override public void mapAtoB(MidlertidigAdresse.MidlertidigGateAdresse fraAdresse,
                            RsMidlertidigAdresse.MidlertidigGateAdresse tilAdresse, MappingContext context) {
                    }
                })
                .byDefault()
                .register();

        factory.classMap(MidlertidigAdresse.MidlertidigStedAdresse.class, RsMidlertidigAdresse.MidlertidigStedAdresse.class)
                .customize(new CustomMapper<MidlertidigAdresse.MidlertidigStedAdresse, RsMidlertidigAdresse.MidlertidigStedAdresse>() {
                    @Override public void mapAtoB(MidlertidigAdresse.MidlertidigStedAdresse fraAdresse,
                            RsMidlertidigAdresse.MidlertidigStedAdresse tilAdresse, MappingContext context) {
                    }
                })
                .byDefault()
                .register();

        factory.classMap(MidlertidigAdresse.MidlertidigPboxAdresse.class, RsMidlertidigAdresse.MidlertidigPboxAdresse.class)
                .customize(new CustomMapper<MidlertidigAdresse.MidlertidigPboxAdresse, RsMidlertidigAdresse.MidlertidigPboxAdresse>() {
                    @Override public void mapAtoB(MidlertidigAdresse.MidlertidigPboxAdresse fraAdresse,
                            RsMidlertidigAdresse.MidlertidigPboxAdresse tilAdresse, MappingContext context) {
                    }
                })
                .byDefault()
                .register();

        factory.classMap(MidlertidigAdresse.MidlertidigUtadAdresse.class, RsMidlertidigAdresse.MidlertidigUtadAdresse.class)
                .customize(new CustomMapper<MidlertidigAdresse.MidlertidigUtadAdresse, RsMidlertidigAdresse.MidlertidigUtadAdresse>() {
                    @Override public void mapAtoB(MidlertidigAdresse.MidlertidigUtadAdresse fraAdresse,
                            RsMidlertidigAdresse.MidlertidigUtadAdresse tilAdresse, MappingContext context) {
                    }
                })
                .byDefault()
                .register();


    }
}