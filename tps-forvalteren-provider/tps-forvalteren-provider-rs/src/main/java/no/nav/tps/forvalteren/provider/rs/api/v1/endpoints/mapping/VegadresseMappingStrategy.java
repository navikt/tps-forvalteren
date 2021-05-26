package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.rs.VegadresseDTO;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsFinnGyldigeAdresserResponse;

@Component
public class VegadresseMappingStrategy implements MappingStrategy {

    @Override public void register(MapperFactory factory) {

        factory.classMap(VegadresseDTO.class, TpsFinnGyldigeAdresserResponse.Adressedata.class)
                .customize(new CustomMapper<>() {
                    @Override public void mapAtoB(VegadresseDTO fraAdresse,
                            TpsFinnGyldigeAdresserResponse.Adressedata tilAdresse, MappingContext context) {

                        tilAdresse.setAdrnavn(fraAdresse.getAdressenavn());
                        tilAdresse.setGkode(fraAdresse.getAdressekode());
                        tilAdresse.setHusnrfra(fraAdresse.getHusnummer().toString());
                        tilAdresse.setHusnrtil(fraAdresse.getHusnummer().toString());
                        tilAdresse.setPnr(fraAdresse.getPostnummer());
                        tilAdresse.setPsted(fraAdresse.getPoststed());
                        tilAdresse.setKnr(fraAdresse.getKommunenummer());
                        tilAdresse.setKnavn(fraAdresse.getKommunenavn());
                    }
                })
                .byDefault()
                .register();
    }
}
