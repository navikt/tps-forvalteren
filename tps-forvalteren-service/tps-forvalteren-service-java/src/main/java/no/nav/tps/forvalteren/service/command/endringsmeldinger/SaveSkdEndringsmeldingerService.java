package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;

@Service
public class SaveSkdEndringsmeldingerService {
    
    @Autowired
    private MessageProvider messageProvider;
    
    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;
    
    @Autowired
    private SaveSkdEndringsmeldingService saveSkdEndringsmeldingService;
    
    public List<Long> save(List<RsMeldingstype> meldinger, Long gruppeId) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(gruppeId);
        List<Long> lagredeMeldingersId = new ArrayList<>();
        if (gruppe != null) {
            for (RsMeldingstype melding : meldinger) {
                SkdEndringsmelding skdEndringsmelding = new SkdEndringsmelding();
                skdEndringsmelding.setGruppe(gruppe);
                SkdEndringsmelding savedMelding = saveSkdEndringsmeldingService.save(melding, skdEndringsmelding);
                lagredeMeldingersId.add(savedMelding.getId());
            }
        } else {
            throw new SkdEndringsmeldingGruppeNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, gruppeId));
        }
        return lagredeMeldingersId;
    }
}
