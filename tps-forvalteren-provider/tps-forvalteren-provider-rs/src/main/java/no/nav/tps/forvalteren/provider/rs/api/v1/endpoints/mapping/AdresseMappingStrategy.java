package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static no.nav.tps.forvalteren.domain.rs.RsRequestAdresse.TilleggType.CO_NAVN;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService.getRandomEtternavn;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService.getRandomFornavn;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.rs.RsRequestAdresse.TilleggAdressetype;
import no.nav.tps.forvalteren.domain.rs.RsRequestGateadresse;
import no.nav.tps.forvalteren.domain.rs.RsRequestMatrikkeladresse;

@Component
public class AdresseMappingStrategy implements MappingStrategy {

    @Override public void register(MapperFactory factory) {
        factory.classMap(RsRequestGateadresse.class, Gateadresse.class)
                .customize(new CustomMapper<RsRequestGateadresse, Gateadresse>() {
                    @Override public void mapAtoB(RsRequestGateadresse fraAdresse,
                            Gateadresse tilAdresse, MappingContext context) {
                        tilAdresse.setAdresse(fraAdresse.getGateadresse());
                        tilAdresse.setTilleggsadresse(getTilleggAdresse(fraAdresse.getTilleggsadresse()));
                    }
                })
                .exclude("tilleggsadresse")
                .byDefault()
                .register();

        factory.classMap(RsRequestMatrikkeladresse.class, Matrikkeladresse.class)
                .customize(new CustomMapper<RsRequestMatrikkeladresse, Matrikkeladresse>() {
                    @Override public void mapAtoB(RsRequestMatrikkeladresse fraAdresse,
                            Matrikkeladresse tilAdresse, MappingContext context) {
                        tilAdresse.setTilleggsadresse(getTilleggAdresse(fraAdresse.getTilleggsadresse()));
                    }
                })
                .exclude("tilleggsadresse")
                .byDefault()
                .register();
    }

    private static String getTilleggAdresse(TilleggAdressetype tilleggAdressetype) {

        if (isNull(tilleggAdressetype)) {
            return null;
        }
        return CO_NAVN == tilleggAdressetype.getTilleggType() ?
                format("C/O %s %s", getRandomFornavn(), getRandomEtternavn()).toUpperCase() :
                format("%s: %s", tilleggAdressetype.getTilleggType(), tilleggAdressetype.getNummer())
                        .replace('_', '-');
    }
}
