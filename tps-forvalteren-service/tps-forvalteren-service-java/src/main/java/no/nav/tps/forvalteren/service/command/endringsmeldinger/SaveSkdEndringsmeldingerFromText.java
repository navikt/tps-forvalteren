package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Service
public class SaveSkdEndringsmeldingerFromText {

    @Autowired
    private SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    private SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    public void execute(List<String> meldinger, Long gruppeId) {
        SkdEndringsmeldingGruppe gruppe = skdEndringsmeldingGruppeRepository.findById(gruppeId);
        if (gruppe != null) {
            for (String melding : meldinger) {
                SkdEndringsmelding skdEndringsmelding = new SkdEndringsmelding();
                skdEndringsmelding.setGruppe(gruppe);
                skdEndringsmelding.setEndringsmelding(melding);
                skdEndringsmeldingRepository.save(skdEndringsmelding);
            }
        } else {
            throw new IllegalArgumentException("skdEndringsmeldingGruppe med id: " + gruppeId + " finnes ikke.");
        }

    }
}
