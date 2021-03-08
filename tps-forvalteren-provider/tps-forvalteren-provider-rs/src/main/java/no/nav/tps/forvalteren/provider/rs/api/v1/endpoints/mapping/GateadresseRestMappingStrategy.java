package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.MapperFactory;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.rs.RsGateadresse;
import org.springframework.stereotype.Component;

@Component
public class GateadresseRestMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsGateadresse.class, Gateadresse.class)
                .field("personId", "person.id")
                .field("gateadresse", "adresse")
                .field("adresseId", "id")
                .byDefault()
                .register();

        factory.classMap(Gateadresse.class, RsGateadresse.class)
                .field("person.id", "personId")
                .field("adresse", "gateadresse")
                .field("id", "adresseId")
                .byDefault()
                .register();
    }

}
