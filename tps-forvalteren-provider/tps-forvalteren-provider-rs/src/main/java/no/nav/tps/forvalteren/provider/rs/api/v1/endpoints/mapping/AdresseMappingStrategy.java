package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.rs.RsRequestGateadresse;
import no.nav.tps.forvalteren.domain.rs.RsRequestMatrikkeladresse;

@Component
public class AdresseMappingStrategy implements MappingStrategy {

    @Override public void register(MapperFactory factory) {
        factory.classMap(RsRequestGateadresse.class, Gateadresse.class)
                .customize(new CustomMapper<RsRequestGateadresse, Gateadresse>() {
                    @Override public void mapAtoB(RsRequestGateadresse fraAdresse,
                            Gateadresse tilAdresse, MappingContext context) {
                        tilAdresse.setAdresse(fraAdresse.getGateadresse());
                    }
                })
                .exclude("tilleggsadresse")
                .byDefault()
                .register();

        factory.classMap(RsRequestMatrikkeladresse.class, Matrikkeladresse.class)
                .customize(new CustomMapper<RsRequestMatrikkeladresse, Matrikkeladresse>() {
                    @Override public void mapAtoB(RsRequestMatrikkeladresse fraAdresse,
                            Matrikkeladresse tilAdresse, MappingContext context) {
                    }
                })
                .exclude("tilleggsadresse")
                .byDefault()
                .register();
    }
}
