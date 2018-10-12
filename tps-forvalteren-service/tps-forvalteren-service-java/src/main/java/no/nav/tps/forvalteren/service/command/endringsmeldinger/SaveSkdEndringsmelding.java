package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_JSON_PROCESSING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
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
    
    public SkdEndringsmelding save(RsMeldingstype melding, SkdEndringsmelding skdEndringsmelding) {
        try {
            String meldingAsJson = mapper.writeValueAsString(melding);
            skdEndringsmelding.setEndringsmelding(meldingAsJson);
            skdEndringsmelding.setAarsakskode(melding.getAarsakskode());
            skdEndringsmelding.setFoedselsnummer(exctractFoedselsnummer(melding));
            skdEndringsmelding.setTransaksjonstype(melding.getTranstype());
            
            return skdEndringsmeldingRepository.save(skdEndringsmelding);
        } catch (JsonProcessingException exception) {
            throw new SkdEndringsmeldingJsonProcessingException(messageProvider.get(SKD_ENDRINGSMELDING_JSON_PROCESSING, melding.getId()), exception);
        }
    }
    
    private String exctractFoedselsnummer(RsMeldingstype melding) {
        if (melding instanceof RsMeldingstype1Felter) {
            return ((RsMeldingstype1Felter) melding).getFodselsdato() + ((RsMeldingstype1Felter) melding).getPersonnummer();
        }
        if (melding instanceof RsMeldingstype2Felter) {
            return ((RsMeldingstype2Felter) melding).getFodselsnr();
        }
        return null;
    }
}
