package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsNewSkdEndringsmelding;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingGruppeNotFoundException;

@Service
public class CreateSkdEndringsmeldingFromType {
    
    @Autowired
    private MessageProvider messageProvider;
    
    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;
    
    @Autowired
    private GetRsMeldingstypeFromTypeText getRsMeldingstypeFromTypeText;
    
    @Autowired
    private SaveSkdEndringsmelding saveSkdEndringsmelding;
    
    public void execute(Long gruppeId, RsNewSkdEndringsmelding rsNewSkdEndringsmelding) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(gruppeId);
        if (gruppe != null) {
            SkdEndringsmelding skdEndringsmelding = new SkdEndringsmelding();
            skdEndringsmelding.setGruppe(gruppe);
            RsMeldingstype melding = getRsMeldingstypeFromTypeText.execute(rsNewSkdEndringsmelding.getMeldingstype());
            melding.setBeskrivelse(rsNewSkdEndringsmelding.getNavn());
            saveSkdEndringsmelding.save(melding, skdEndringsmelding);
        } else {
            throw new SkdEndringsmeldingGruppeNotFoundException(messageProvider.get(SKD_ENDRINGSMELDING_GRUPPE_NOT_FOUND, gruppeId));
        }
    }
    
}
