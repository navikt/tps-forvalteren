package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_PROCESSING;
import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_NOT_FOUND;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonProcessingException;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingNotFoundException;

@Service
public class UpdateSkdEndringsmelding {

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    private ObjectMapper mapper;

    public void execute(List<RsMeldingstype> meldinger) {
        for (RsMeldingstype melding : meldinger) {
            SkdEndringsmelding skdEndringsmelding = skdEndringsmeldingRepository.findById(melding.getId());
            if (skdEndringsmelding != null) {
                try {
                    String meldingAsJson = mapper.writeValueAsString(melding);
                    skdEndringsmelding.setEndringsmelding(meldingAsJson);
                    skdEndringsmeldingRepository.save(skdEndringsmelding);
                } catch (JsonProcessingException exception) {
                    throw new SkdEndringsmeldingJsonProcessingException(messageProvider.get(SKD_ENDRINGSMELDING_JSON_PROCESSING, melding.getId()), exception);
                }
            } else {
                throw new SkdEndringsmeldingNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_NOT_FOUND, melding.getId()));
            }
        }
    }

}