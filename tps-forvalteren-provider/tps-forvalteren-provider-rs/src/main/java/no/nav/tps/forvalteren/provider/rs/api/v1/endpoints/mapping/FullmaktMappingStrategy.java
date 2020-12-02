package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import java.util.Arrays;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Fullmakt;
import no.nav.tps.forvalteren.domain.rs.RsFullmakt;
import no.nav.tps.forvalteren.domain.rs.RsSimplePerson;
import no.nav.tps.forvalteren.domain.rs.dolly.RsFullmaktRequest;

@Component
public class FullmaktMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Fullmakt.class, RsFullmakt.class)
                .customize(new CustomMapper<>() {
                    @Override
                    public void mapAtoB(Fullmakt fullmakt, RsFullmakt rsFullmakt, MappingContext context) {
                        rsFullmakt.setId(fullmakt.getId());
                        rsFullmakt.setFullmektig(mapperFacade.map(fullmakt.getFullmektig(), RsSimplePerson.class));
                        rsFullmakt.setGyldigFom(fullmakt.getGyldigFom());
                        rsFullmakt.setGyldigTom(fullmakt.getGyldigTom());
                        rsFullmakt.setKilde(fullmakt.getKilde());
                        rsFullmakt.setOmraader(Arrays.asList(fullmakt.getOmraader().split(",")));
                    }
                })
                .register();

        factory.classMap(RsFullmaktRequest.class, Fullmakt.class)
                .customize(new CustomMapper<>() {
                    @Override
                    public void mapAtoB(RsFullmaktRequest rsFullmakt, Fullmakt fullmakt, MappingContext context) {
                        fullmakt.setOmraader(String.join(",",rsFullmakt.getOmraader()));
                    }
                })
                .exclude("omraader")
                .byDefault()
                .register();
    }
}
