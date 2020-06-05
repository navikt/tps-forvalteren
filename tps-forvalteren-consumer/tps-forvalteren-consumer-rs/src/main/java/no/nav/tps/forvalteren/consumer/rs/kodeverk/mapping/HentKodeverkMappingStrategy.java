package no.nav.tps.forvalteren.consumer.rs.kodeverk.mapping;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.consumer.rs.kodeverk.KodeverkResponse;
import no.nav.tps.forvalteren.domain.rs.kodeverk.Kodeverk;

@Component
public class HentKodeverkMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(KodeverkResponse.class, Kodeverk.class)
                .customize(
                        new CustomMapper<KodeverkResponse, Kodeverk>() {
                            @Override public void mapAtoB(KodeverkResponse from,
                                    Kodeverk to, MappingContext context) {

                                to.setKoder(from.getBetydninger().entrySet().stream()
                                        .filter(entry -> entry.getValue().size() > 0)
                                        .map(entry ->
                                                no.nav.tps.forvalteren.domain.rs.kodeverk.Kode.builder()
                                                        .navn(entry.getKey())
                                                        .fom(entry.getValue().get(0).getGyldigFra())
                                                        .tom(entry.getValue().get(0).getGyldigTil())
                                                        .term(entry.getValue().get(0).getBeskrivelser().getNorsk().getTerm())
                                                        .uri(entry.getValue().get(0).getBeskrivelser().getNorsk().getTekst())
                                                        .build()
                                        )
                                        .collect(Collectors.toList())
                                );
                            }
                        }
                )
                .register();
    }
}
