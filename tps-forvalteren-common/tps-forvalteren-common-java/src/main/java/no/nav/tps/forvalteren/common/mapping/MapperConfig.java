package no.nav.tps.forvalteren.common.mapping;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Configuration
public class MapperConfig {

    @Autowired(required = false)
    private List<MappingStrategy> mappingStrategies = new ArrayList<>();

    @Bean
    public MapperFacade mapperFacade() {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        for (MappingStrategy mapper : mappingStrategies) {
            mapper.register(mapperFactory);
        }

        return mapperFactory.getMapperFacade();
    }
}