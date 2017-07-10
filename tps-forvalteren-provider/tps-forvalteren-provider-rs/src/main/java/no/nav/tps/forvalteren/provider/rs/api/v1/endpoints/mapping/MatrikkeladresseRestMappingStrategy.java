package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.MapperFactory;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.rs.RsMatrikkeladresse;
import org.springframework.stereotype.Component;

@Component
public class MatrikkeladresseRestMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsMatrikkeladresse.class, Matrikkeladresse.class).byDefault().register();
        factory.classMap(Matrikkeladresse.class, RsMatrikkeladresse.class).byDefault().register();
    }
}
