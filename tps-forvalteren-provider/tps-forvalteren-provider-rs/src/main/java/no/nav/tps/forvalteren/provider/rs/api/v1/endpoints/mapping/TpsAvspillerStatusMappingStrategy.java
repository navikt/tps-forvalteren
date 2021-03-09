package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.TpsAvspiller;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsAvspiller;

@Component
public class TpsAvspillerStatusMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(TpsAvspiller.class, RsTpsAvspiller.class)
                .customize(
                        new CustomMapper<TpsAvspiller, RsTpsAvspiller>() {
                            @Override
                            public void mapAtoB(TpsAvspiller avspiller, RsTpsAvspiller status, MappingContext context) {
                                avspiller.getProgressList().forEach(progress ->
                                        status.getProgressMap().put(progress.getIndeksNr(), progress));
                                status.setProgressAntall(avspiller.getProgressList().size());
                                status.setError(avspiller.getFeil());
                            }
                        })
                .byDefault()
                .register();
    }
}