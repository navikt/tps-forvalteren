package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_PROCESSING;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonProcessingException;

@Service
public class SaveSkdEndringsmeldingerFromText {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    public void execute(List<RsMeldingstype> meldinger, Long gruppeId) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(gruppeId);
        if (gruppe != null) {
            for (RsMeldingstype melding : meldinger) {
                SkdEndringsmelding skdEndringsmelding = new SkdEndringsmelding();
                skdEndringsmelding.setGruppe(gruppe);
                try {
                    String meldingAsJson = mapper.writeValueAsString(melding);
                    skdEndringsmelding.setEndringsmelding(meldingAsJson);
                    skdEndringsmeldingRepository.save(skdEndringsmelding);
                } catch (JsonProcessingException exception) {
                    throw new SkdEndringsmeldingJsonProcessingException(messageProvider.get(SKD_ENDRINGSMELDING_JSON_PROCESSING, melding.getId()), exception);
                }

            }
        } else {
            throw new SkdEndringsmeldingGruppeNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, gruppeId));
        }

    }
}
