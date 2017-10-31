package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@Service
public class SaveSkdEndringsmeldingGruppe {

    @Autowired
    private SkdEndringsmeldingGruppeRepository repository;

    public void execute(SkdEndringsmeldingGruppe gruppe) {
        repository.save(gruppe);
    }

}
