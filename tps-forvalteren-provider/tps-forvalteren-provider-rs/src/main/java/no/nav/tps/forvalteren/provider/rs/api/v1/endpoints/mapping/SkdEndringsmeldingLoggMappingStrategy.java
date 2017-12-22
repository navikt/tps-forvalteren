package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingLogg;

@Component
public class SkdEndringsmeldingLoggMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(SkdEndringsmeldingLogg.class, RsSkdEndringsmeldingLogg.class)
                .field("endringsmelding", "raw")
                .field("environment", "miljoe")
                .field("innsendtDato", "utfoertDato")
                .field("innsendtAv", "utfoertAv")
                .byDefault()
                .register();
    }

}
