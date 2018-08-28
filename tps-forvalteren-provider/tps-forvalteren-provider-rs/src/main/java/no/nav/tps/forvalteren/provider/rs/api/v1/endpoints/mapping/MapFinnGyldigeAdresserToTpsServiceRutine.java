package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserRequest;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.request.param.FinnGyldigeAdresserRequestParam;

@Component
public class MapFinnGyldigeAdresserToTpsServiceRutine {
    private MapperFacade mapper = constructMapper();
    
    public TpsFinnGyldigeAdresserRequest map(FinnGyldigeAdresserRequestParam restRequestParam, TpsFinnGyldigeAdresserRequest tpsFinnGyldigeAdresserRequest) {
        mapper.map(restRequestParam, tpsFinnGyldigeAdresserRequest);
        return tpsFinnGyldigeAdresserRequest;
    }
    
    private MapperFacade constructMapper() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();
        mapperFactory.classMap(FinnGyldigeAdresserRequestParam.class, TpsFinnGyldigeAdresserRequest.class).byDefault();
        return mapperFactory.getMapperFacade();
    }
}
