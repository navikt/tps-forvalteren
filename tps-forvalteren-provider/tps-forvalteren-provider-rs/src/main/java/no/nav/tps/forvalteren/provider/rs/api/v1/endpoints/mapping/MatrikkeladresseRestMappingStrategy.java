package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.rs.RsMatrikkeladresse;

@Component
public class MatrikkeladresseRestMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsMatrikkeladresse.class, Matrikkeladresse.class)
                .field("personId", "person.id")
                .field("adresseId", "id")
                .byDefault()
                .register();

        factory.classMap(Matrikkeladresse.class, RsMatrikkeladresse.class)
                .field("person.id", "personId")
                .field("id", "adresseId")
                .byDefault()
                .register();
    }
}
