package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.rs.RsGruppe;
import no.nav.tps.forvalteren.domain.rs.RsSimpleGruppe;

@Component
public class GruppeRestMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Gruppe.class, RsSimpleGruppe.class).byDefault().register();

        factory.classMap(Gruppe.class, RsGruppe.class).byDefault().register();
    }
}
