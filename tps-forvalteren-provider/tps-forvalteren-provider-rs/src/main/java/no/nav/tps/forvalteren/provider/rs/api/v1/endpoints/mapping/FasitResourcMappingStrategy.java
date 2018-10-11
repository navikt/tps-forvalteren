package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResource;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitResourceWithUnmappedProperties;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitPropertyTypes;
import no.nav.tps.forvalteren.consumer.rs.environments.resourcetypes.FasitProperty;

import org.springframework.stereotype.Component;

@Component
public class FasitResourcMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(FasitResourceWithUnmappedProperties.class, FasitResource.class)
                .field("id", "id")
                .field("type", "type")
                .field("alias", "alias")
                .field("created", "created")
                .field("updated", "updated")
                .field("dodgy", "dodgy")
                .field("scope", "scope")
                .customize(new CustomMapper<FasitResourceWithUnmappedProperties, FasitResource>() {
                    @Override
                    public void mapAtoB(FasitResourceWithUnmappedProperties fasitResourceWithUnmappedProperties, FasitResource fasitResource, MappingContext context) {
                        Class<? extends FasitProperty> fasitPropertyClass = FasitPropertyTypes.getEnumByName(fasitResourceWithUnmappedProperties.getType()).getFasitPropertyClass();
                        fasitResource.setProperties(mapperFacade.map(fasitResourceWithUnmappedProperties.getProperties(), fasitPropertyClass));
                    }
                })
                .register();
    }
}
