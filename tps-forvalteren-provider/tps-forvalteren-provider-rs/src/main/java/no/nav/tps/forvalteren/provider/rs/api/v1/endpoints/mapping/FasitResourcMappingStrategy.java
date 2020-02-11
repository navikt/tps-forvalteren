package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResourceWithUnmappedProperties;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitProperty;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;

@Component
public class FasitResourcMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(FasitResourceWithUnmappedProperties.class, FasitResource.class)
                .customize(new CustomMapper<FasitResourceWithUnmappedProperties, FasitResource>() {
                    @Override
                    public void mapAtoB(FasitResourceWithUnmappedProperties fasitResourceWithUnmappedProperties, FasitResource fasitResource, MappingContext context) {
                        Class<? extends FasitProperty> fasitPropertyClass = FasitPropertyTypes.getEnumByName(fasitResourceWithUnmappedProperties.getType()).getFasitPropertyClass();
                        fasitResource.setProperties(mapperFacade.map(fasitResourceWithUnmappedProperties.getProperties(), fasitPropertyClass));
                    }
                })
                .exclude("properties")
                .byDefault()
                .register();
    }
}
