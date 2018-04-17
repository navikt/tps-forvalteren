package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import org.springframework.stereotype.Component;

@Component
public class PersonmalMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsPersonMal.class, Personmal.class)
                .customize(new CustomMapper<RsPersonMal, Personmal>() {
                    @Override
                    public void mapAtoB(RsPersonMal rsPersonMal, Personmal personmal, MappingContext mappingContext) {
                        personmal.setMaxAntallBarn(Integer.parseInt(rsPersonMal.getMaxAntallBarn()));
                        personmal.setMinAntallBarn(Integer.parseInt(rsPersonMal.getMinAntallBarn()));
                    }
                })
                .field("bruker", "opprettetAv")
                .byDefault()
                .register();

        factory.classMap(Personmal.class, RsPersonMal.class)
                .customize(new CustomMapper<Personmal, RsPersonMal>() {
                    public void mapBtoA(Personmal personmal, RsPersonMal rsPersonMal, MappingContext mappingContext) {
                        rsPersonMal.setMaxAntallBarn(String.valueOf(personmal.getMaxAntallBarn()));
                        rsPersonMal.setMinAntallBarn(String.valueOf(personmal.getMinAntallBarn()));

                    }
                })
                .field("opprettetAv", "bruker")
                .byDefault()
                .register();

    }
}
