package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_TO_OBJECT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.mapping.MappingStrategy;
import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsSkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonToObjectException;

@Component
public class SkdEndringsmeldingGruppeMappingStrategy implements MappingStrategy {

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(SkdEndringsmeldingGruppe.class, RsSkdEndringsmeldingGruppe.class)
                .customize(
                        new CustomMapper<SkdEndringsmeldingGruppe, RsSkdEndringsmeldingGruppe>() {
                            @Override
                            public void mapAtoB(SkdEndringsmeldingGruppe skdEndringsmeldingGruppe, RsSkdEndringsmeldingGruppe rsSkdEndringsmeldingGruppe, MappingContext context) {
                                List<RsMeldingstype> meldinger = new ArrayList<>();
                                for (SkdEndringsmelding melding : skdEndringsmeldingGruppe.getSkdEndringsmeldinger()) {
                                    try {
                                        RsMeldingstype newMelding = mapper.readValue(melding.getEndringsmelding(), RsMeldingstype.class);
                                        newMelding.setId(melding.getId());
                                        meldinger.add(newMelding);
                                    } catch (IOException exception) {
                                        throw new SkdEndringsmeldingJsonToObjectException(messageProvider.get(SKD_ENDRINGSMELDING_JSON_TO_OBJECT, melding.getId()), exception);
                                    }
                                }
                                rsSkdEndringsmeldingGruppe.setMeldinger(meldinger);
                            }

                            @Override
                            public void mapBtoA(RsSkdEndringsmeldingGruppe rsSkdEndringsmeldingGruppe, SkdEndringsmeldingGruppe skdEndringsmeldingGruppe, MappingContext context) {

                            }
                        })
                .byDefault()
                .register();
    }

}