package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRow;

@Component
public class DeathRowMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(DeathRow.class, RsDeathRow.class)
                .field("endretDato", "bruker")
                .field("endretAv", "tidspunkt")
                .byDefault()
                .register();
        
        factory.classMap(RsDeathRow.class, DeathRow.class)
                .byDefault()
                .register();
    }

}
