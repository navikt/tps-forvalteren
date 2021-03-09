package no.nav.tps.forvalteren.provider.rs.util;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;

public class MapperTestUtils {

    public static MapperFacade createMapperFacadeForMappingStrategy(MappingStrategy... strategies) {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        for (MappingStrategy strategy : strategies) {
            strategy.register(mapperFactory);
        }

        return mapperFactory.getMapperFacade();
    }
}
