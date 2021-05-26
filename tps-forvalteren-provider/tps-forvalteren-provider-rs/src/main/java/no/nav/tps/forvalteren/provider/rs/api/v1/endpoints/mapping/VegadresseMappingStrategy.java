package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.util.Objects.nonNull;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
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
                        tilAdresse.setHusnrfra(nonNull(fraAdresse.getHusnummer()) ? fraAdresse.getHusnummer().toString() : null);
                        tilAdresse.setHusnrtil(nonNull(fraAdresse.getHusnummer()) ? fraAdresse.getHusnummer().toString() : null);
                        tilAdresse.setPnr(fraAdresse.getPostnummer());
                        tilAdresse.setPsted(fraAdresse.getPoststed());
                        tilAdresse.setKnr(fraAdresse.getKommunenummer());
                        tilAdresse.setKnavn(fraAdresse.getKommunenavn());
                    }
                })
                .byDefault()
                .register();

        factory.classMap(VegadresseDTO.class, Gateadresse.class)
                .customize(new CustomMapper<>() {
                    @Override public void mapAtoB(VegadresseDTO fraAdresse,
                            Gateadresse tilAdresse, MappingContext context) {

                        tilAdresse.setAdresse(fraAdresse.getAdressenavn());
                        tilAdresse.setGatekode(fraAdresse.getAdressekode());
                        tilAdresse.setHusnummer(nonNull(fraAdresse.getHusnummer()) ? fraAdresse.getHusnummer().toString() : null);
                        tilAdresse.setPostnr(fraAdresse.getPostnummer());
                        tilAdresse.setKommunenr(fraAdresse.getKommunenummer());
                    }
                })
                .byDefault()
                .register();
    }
}
