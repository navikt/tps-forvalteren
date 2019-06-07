package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.rs.RsAvspillerRequest;
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
    }
}
