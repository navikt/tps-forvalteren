package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.MapperFactory;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.domain.rs.RsVergemaal;
import org.springframework.stereotype.Component;

@Component
public class VergemaalMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Vergemaal.class, RsVergemaal.class)
                .byDefault()
                .register();

        factory.classMap(RsVergemaal.class, Vergemaal.class)
                .byDefault()
                .register();
    }
}
