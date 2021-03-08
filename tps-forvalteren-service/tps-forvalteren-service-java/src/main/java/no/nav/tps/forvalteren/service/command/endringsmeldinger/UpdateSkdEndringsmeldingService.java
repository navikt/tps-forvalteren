package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ENDRINGSMELDING_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingNotFoundException;

@Service
public class UpdateSkdEndringsmeldingService {
    
    @Autowired
    private MessageProvider messageProvider;
    
    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;
    
    @Autowired
    private SaveSkdEndringsmeldingService saveSkdEndringsmeldingService;
    
    public void update(List<RsMeldingstype> meldinger) {
        for (RsMeldingstype melding : meldinger) {
            Optional<SkdEndringsmelding> skdEndringsmelding = skdEndringsmeldingRepository.findById(melding.getId());
            if (skdEndringsmelding.isPresent()) {
                saveSkdEndringsmeldingService.save(melding, skdEndringsmelding.get());
            } else {
                throw new SkdEndringsmeldingNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_NOT_FOUND, melding.getId()));
            }
        }
    }
    
}