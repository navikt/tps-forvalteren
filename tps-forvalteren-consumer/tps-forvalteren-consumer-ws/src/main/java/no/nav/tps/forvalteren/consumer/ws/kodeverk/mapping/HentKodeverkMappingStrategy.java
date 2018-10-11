package no.nav.tps.forvalteren.consumer.ws.kodeverk.mapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.EnkeltKodeverk;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Kode;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Kodeverk;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Kodeverkselement;
import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Term;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;

@Component
public class HentKodeverkMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.registerMapper(customMapper());
    }

    private Mapper<Kodeverk, no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk> customMapper() {
        return new CustomMapper<Kodeverk, no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk>() {
            @Override
            public void mapAtoB(Kodeverk from, no.nav.tps.forvalteren.domain.ws.kodeverk.Kodeverk to, MappingContext context) {
                EnkeltKodeverk fromKodeverk = (EnkeltKodeverk) from;

                to.setNavn(fromKodeverk.getNavn());
                to.setUri(fromKodeverk.getUri());
                to.setVersjon(Integer.valueOf(fromKodeverk.getVersjonsnummer()));
                to.setFom(mapGyldigFom(fromKodeverk));
                to.setTom(mapGyldigTom(fromKodeverk));
                to.setKoder(mapCodes(fromKodeverk.getKode()));
            }
        };
    }

    private List<no.nav.tps.forvalteren.domain.ws.kodeverk.Kode> mapCodes(List<Kode> codesToMap) {
        List<no.nav.tps.forvalteren.domain.ws.kodeverk.Kode> mappedCodes = new ArrayList<>();
        for (Kode fromCode : codesToMap) {
            mappedCodes.addAll(mapCode(fromCode));
        }
        return mappedCodes;
    }

    private List<no.nav.tps.forvalteren.domain.ws.kodeverk.Kode> mapCode(Kode fromCode) {
        List<no.nav.tps.forvalteren.domain.ws.kodeverk.Kode> mappedCodes = Lists.newArrayListWithExpectedSize(fromCode.getTerm().size());
        for (Term fromTerm : fromCode.getTerm()) {
            no.nav.tps.forvalteren.domain.ws.kodeverk.Kode toCode = new no.nav.tps.forvalteren.domain.ws.kodeverk.Kode();
            toCode.setNavn(fromCode.getNavn());
            toCode.setTerm(fromTerm.getNavn());
            toCode.setFom(mapGyldigFom(fromTerm));
            toCode.setTom(mapGyldigTom(fromTerm));
            toCode.setUri(fromCode.getUri());
            mappedCodes.add(toCode);
        }
        return mappedCodes;
    }

    private LocalDate mapGyldigFom(Kodeverkselement kodeverkselement) {
        long milliFom = kodeverkselement.getGyldighetsperiode().get(0).getFom().toGregorianCalendar().getTimeInMillis();
        return Instant.ofEpochMilli(milliFom).atZone(ZoneId.systemDefault()).toLocalDate();

    }

    private LocalDate mapGyldigTom(Kodeverkselement kodeverkselement) {
        long milliTom = kodeverkselement.getGyldighetsperiode().get(0).getTom().toGregorianCalendar().getTimeInMillis();
        return Instant.ofEpochMilli(milliTom).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
