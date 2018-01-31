package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import java.util.List;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRowBulk;

@Component
public class DeathRowMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(DeathRow.class, RsDeathRow.class)
                .field("endretDato", "bruker")
                .field("endretAv", "tidspunkt").byDefault().register();

        factory.classMap(RsDeathRow.class, DeathRow.class).byDefault().register();

        factory.classMap(RsDeathRowBulk.class, List.class)
                .customize(new CustomMapper<RsDeathRowBulk, List>() {
                    @Override
                    public void mapAtoB(RsDeathRowBulk rsDeathRowBulk, List deathRowList, MappingContext context) {
                        rsDeathRowBulk.getIdenter().forEach(ident -> {
                            deathRowList.add(DeathRow.builder()
                                    .ident(ident)
                                    .tilstand(rsDeathRowBulk.getTilstand())
                                    .status(rsDeathRowBulk.getStatus())
                                    .miljoe(rsDeathRowBulk.getMiljoe())
                                    .handling(rsDeathRowBulk.getHandling())
                                    .doedsdato(rsDeathRowBulk.getDoedsdato())
                                    .build());
                        });
                    }
                })
                .register();
    }
}
