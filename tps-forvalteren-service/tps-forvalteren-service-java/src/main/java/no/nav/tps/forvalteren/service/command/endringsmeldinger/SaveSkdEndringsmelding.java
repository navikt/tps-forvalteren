package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_PROCESSING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonProcessingException;

@Service
public class SaveSkdEndringsmelding {

    @Autowired
    private MessageProvider messageProvider;
    
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;
    
    public void execute(RsMeldingstype melding, SkdEndringsmelding skdEndringsmelding) {
        try {
            String meldingAsJson = mapper.writeValueAsString(melding);
            skdEndringsmelding.setEndringsmelding(meldingAsJson);
            skdEndringsmeldingRepository.save(skdEndringsmelding);
        } catch (JsonProcessingException exception) {
            throw new SkdEndringsmeldingJsonProcessingException(messageProvider.get(SKD_ENDRINGSMELDING_JSON_PROCESSING, melding.getId()), exception);
        }
    }
}
