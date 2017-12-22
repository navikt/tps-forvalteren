package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_TO_OBJECT;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingJsonToObjectException;

@Service
public class ConvertJsonToRsMeldingstype {

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private ObjectMapper mapper;

    public RsMeldingstype execute(SkdEndringsmelding melding) {
        try {
            RsMeldingstype newMelding = mapper.readValue(melding.getEndringsmelding(), RsMeldingstype.class);
            newMelding.setId(melding.getId());
            return newMelding;
        } catch (IOException exception) {
            throw new SkdEndringsmeldingJsonToObjectException(messageProvider.get(SKD_ENDRINGSMELDING_JSON_TO_OBJECT, melding.getId()), exception);
        }
    }

}
