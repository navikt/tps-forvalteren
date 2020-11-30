package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Fullmakt;
import no.nav.tps.forvalteren.domain.rs.RsFullmakt;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class FullmaktMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Fullmakt.class, RsFullmakt.class)
                .byDefault()
                .exclude("omraader")
                .customize(new CustomMapper<>() {
                    @Override
                    public void mapAtoB(Fullmakt fullmakt, RsFullmakt rsFullmakt, MappingContext context) {
                        rsFullmakt.setOmraader(Arrays.stream(fullmakt.getOmraader().split(",")).collect(Collectors.toList()));
                    }
                })
                .register();

        factory.classMap(RsFullmakt.class, Fullmakt.class)
                .byDefault()
                .exclude("omraader")
                .customize(new CustomMapper<>() {
                    @Override
                    public void mapAtoB(RsFullmakt rsFullmakt, Fullmakt fullmakt, MappingContext context) {
                        fullmakt.setOmraader(rsFullmakt.getOmraader().stream().map(Object::toString).collect(Collectors.joining(",")));
                    }
                })
                .register();
    }
}
