package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.lang.Long.valueOf;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
import no.nav.tps.forvalteren.domain.rs.RsMelding;
import no.nav.tps.forvalteren.domain.rs.RsMeldingerResponse;
import no.nav.tps.xjc.ctg.domain.s302.HendelsedataFraTpsS302;
import no.nav.tps.xjc.ctg.domain.s302.TpsPersonData;
import no.nav.tps.xjc.ctg.domain.s302.TpsServiceRutineType;

@Component
public class TpsAvspillerMappingStrategy implements MappingStrategy {

    private static final String TIME_PATTERN = "HH:mm:ss";

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsAvspillerRequest.class, TpsPersonData.class)
                .customize(
                        new CustomMapper<RsAvspillerRequest, TpsPersonData>() {
                            @Override
                            public void mapAtoB(RsAvspillerRequest request, TpsPersonData tpsPersonData, MappingContext context) {

                                tpsPersonData.setTpsServiceRutine(new TpsServiceRutineType());

                                tpsPersonData.getTpsServiceRutine().setMeldingFormat(request.getFormat().getMeldingFormat());
                                tpsPersonData.getTpsServiceRutine().setFraDato(xtractDate(request.getDatoFra()));
                                tpsPersonData.getTpsServiceRutine().setFraTid(xtractTime(request.getDatoFra()));
                                tpsPersonData.getTpsServiceRutine().setTilDato(xtractDate(request.getDatoTil()));
                                tpsPersonData.getTpsServiceRutine().setTilTid(xtractTime(request.getDatoTil()));
                                tpsPersonData.getTpsServiceRutine().setMeldingType(convertMeldingType(request.getTyper()));
                                tpsPersonData.getTpsServiceRutine().setKildeSystem(convertKildeSystem(request.getKilder()));
                                tpsPersonData.getTpsServiceRutine().setFnr(convertIdenter(request.getIdenter()));
                                tpsPersonData.getTpsServiceRutine().setSideNummer(Long.toString(request.getPageNumber() + 1));
                                tpsPersonData.getTpsServiceRutine().setAntallRaderprSide(request.getBufferSize().toString());
                            }
                        })

                .byDefault()
                .register();

        factory.classMap(HendelsedataFraTpsS302.class, RsMeldingerResponse.class)
                .customize(
                        new CustomMapper<HendelsedataFraTpsS302, RsMeldingerResponse>() {
                            @Override
                            public void mapAtoB(HendelsedataFraTpsS302 hendelsedata, RsMeldingerResponse meldingerResponse, MappingContext context) {

                                if (valueOf(hendelsedata.getMeldingerTotalt()) > 0) {
                                    AtomicLong index = new AtomicLong(valueOf(hendelsedata.getAntallRaderprSide()) *
                                            (isNotBlank(hendelsedata.getSideNummer()) ? valueOf(hendelsedata.getSideNummer()) - 1 : 0));

                                    meldingerResponse.setAntallTotalt(valueOf(hendelsedata.getMeldingerTotalt()));
                                    meldingerResponse.setBuffernumber(isNotBlank(hendelsedata.getSideNummer()) ? valueOf(hendelsedata.getSideNummer()) : null);
                                    meldingerResponse.setBuffersize(valueOf(hendelsedata.getAntallRaderprSide()));

                                    meldingerResponse.setMeldinger(hendelsedata.getMelding().getEnmelding()
                                            .stream()
                                            .map(type -> RsMelding.builder()
                                                    .index(index.getAndIncrement() + 1)
                                                    .meldingNummer(type.getMNr())
                                                    .tidspunkt(nonNull(type.getMTd()) ? convertToTimestamp(type.getMTd()) : null)
                                                    .hendelseType(type.getMTp())
                                                    .systemkilde(type.getMKs())
                                                    .ident(type.getMFnr())
                                                    .build())
                                            .collect(Collectors.toList()));
                                }
                            }
                        })
                .register();
    }

    private LocalDateTime convertToTimestamp(String timestamp) {
        if (isNull(timestamp)) {
            return null;
        }
        return timestamp.length() > 10 ?
                LocalDateTime.parse(timestamp) :
                LocalDate.parse(timestamp).atStartOfDay();
    }

    private static String xtractDate(LocalDateTime localDateTime) {
        return nonNull(localDateTime) ? localDateTime.toLocalDate().toString() : null;
    }

    private static String xtractTime(LocalDateTime localDateTime) {
        return nonNull(localDateTime) ? localDateTime.format(DateTimeFormatter.ofPattern(TIME_PATTERN)) : null;
    }

    private static TpsServiceRutineType.MeldingType convertMeldingType(List<String> meldingstyper) {
        TpsServiceRutineType.MeldingType meldingType = new TpsServiceRutineType.MeldingType();
        meldingType.getEnMeldingType().addAll(meldingstyper);
        return meldingType;
    }

    private static TpsServiceRutineType.KildeSystem convertKildeSystem(List<String> kilder) {
        TpsServiceRutineType.KildeSystem kildeSystem = new TpsServiceRutineType.KildeSystem();
        kildeSystem.getEtKildeSystem().addAll(kilder);
        return kildeSystem;
    }

    private static TpsServiceRutineType.Fnr convertIdenter(List<String> identer) {
        TpsServiceRutineType.Fnr fnr = new TpsServiceRutineType.Fnr();
        fnr.getEtFnr().addAll(identer);
        return fnr;
    }
}
