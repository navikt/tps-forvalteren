package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.lang.Long.valueOf;
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
public class TpsAvspillerRequestMappingStrategy implements MappingStrategy {

    private static final String TIME_PATTERN = "HH:mm:ss";

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsAvspillerRequest.class, TpsPersonData.class)
                .customize(
                        new CustomMapper<RsAvspillerRequest, TpsPersonData>() {
                            @Override
                            public void mapAtoB(RsAvspillerRequest request, TpsPersonData tpsPersonData, MappingContext context) {

                                TpsServiceRutineType tpsServiceRutineType = new TpsServiceRutineType();
                                tpsServiceRutineType.setMeldingFormat(request.getFormat().getMeldingFormat());
                                tpsServiceRutineType.setFraDato(request.getDatoFra().toLocalDate().toString());
                                tpsServiceRutineType.setFraTid(request.getDatoFra().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
                                tpsServiceRutineType.setTilDato(request.getDatoTil().toLocalDate().toString());
                                tpsServiceRutineType.setTilTid(request.getDatoTil().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
                                tpsServiceRutineType.setMeldingType(convertMeldingType(request.getTyper()));
                                tpsServiceRutineType.setKildeSystem(convertKildeSystem(request.getKilder()));
                                tpsServiceRutineType.setFnr(convertIdenter(request.getIdenter()));

                                tpsPersonData.setTpsServiceRutine(tpsServiceRutineType);
                            }

                            private TpsServiceRutineType.MeldingType convertMeldingType(List<String> meldingstyper) {
                                TpsServiceRutineType.MeldingType meldingType = new TpsServiceRutineType.MeldingType();
                                meldingType.getEnMeldingType().addAll(meldingstyper);
                                return meldingType;
                            }

                            private TpsServiceRutineType.KildeSystem convertKildeSystem(List<String> kilder) {
                                TpsServiceRutineType.KildeSystem kildeSystem = new TpsServiceRutineType.KildeSystem();
                                kildeSystem.getEtKildeSystem().addAll(kilder);
                                return kildeSystem;
                            }

                            private TpsServiceRutineType.Fnr convertIdenter(List<String> identer) {
                                TpsServiceRutineType.Fnr fnr = new TpsServiceRutineType.Fnr();
                                fnr.getEtFnr().addAll(identer);
                                return fnr;
                            }
                        })

                .byDefault()
                .register();

        factory.classMap(HendelsedataFraTpsS302.class, RsMeldingerResponse.class)
                .customize(
                        new CustomMapper<HendelsedataFraTpsS302, RsMeldingerResponse>() {
                            @Override
                            public void mapAtoB(HendelsedataFraTpsS302 hendelsedata, RsMeldingerResponse meldingerResponse, MappingContext context) {

                                AtomicLong index = new AtomicLong(Long.valueOf(hendelsedata.getAntallRaderprSide()) *
                                        (isNotBlank(hendelsedata.getMeldingNummer()) ? Long.valueOf(hendelsedata.getMeldingNummer()) - 1 : 0));

                                meldingerResponse.setAntallTotalt(isNotBlank(hendelsedata.getMeldingerTotalt()) ? valueOf(hendelsedata.getMeldingerTotalt()) : 0);
                                meldingerResponse.setBuffernumber(isNotBlank(hendelsedata.getMeldingNummer()) ? valueOf(hendelsedata.getMeldingNummer()) : null);
                                meldingerResponse.setBuffersize(Long.valueOf(hendelsedata.getAntallRaderprSide()));

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

                            private LocalDateTime convertToTimestamp(String timestamp) {
                                return nonNull(timestamp) ?
                                        timestamp.length() > 10 ?
                                                LocalDateTime.parse(timestamp) :
                                                LocalDate.parse(timestamp).atStartOfDay() :
                                        null;
                            }
                        })
                .register();
    }
}
