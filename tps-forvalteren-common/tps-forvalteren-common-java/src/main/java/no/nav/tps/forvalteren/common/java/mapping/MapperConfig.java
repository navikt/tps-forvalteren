package no.nav.tps.forvalteren.common.java.mapping;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.rs.RsGateadresse;
import no.nav.tps.forvalteren.domain.rs.RsMatrikkeladresse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MapperConfig {

    @Autowired(required = false)
    private List<MappingStrategy> mappingStrategies;

    @Bean
    MapperFacade mapperFacade() {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(RsGateadresse.class, Gateadresse.class).byDefault().register();
        mapperFactory.classMap(Gateadresse.class, RsGateadresse.class).byDefault().register();
        mapperFactory.classMap(RsMatrikkeladresse.class, Matrikkeladresse.class).byDefault().register();
        mapperFactory.classMap(Matrikkeladresse.class, RsMatrikkeladresse.class).byDefault().register();

        if (mappingStrategies != null) {
            for (MappingStrategy mapper : mappingStrategies) {
                mapper.register(mapperFactory);

            }
        }

        return mapperFactory.getMapperFacade();
    }
}