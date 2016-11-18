package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import no.nav.tps.vedlikehold.domain.rs.testdata.RsTestDataRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.testdata.TestDataRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Rizwan Ali Ahmed, Visma Consulting AS.
 */
@Configuration
public abstract class TestDataMappingConfig {

    @Bean
    public MapperFacade testDataMapper(){
        final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        configureTestDataRequest(mapperFactory);
        return mapperFactory.getMapperFacade();
    }

    private void configureTestDataRequest(MapperFactory mapperFactory){
        mapperFactory.classMap(RsTestDataRequest.class, TestDataRequest.class)
                .register();
    }
}
