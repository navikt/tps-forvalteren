package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@Service
public class DeleteSkdEndringsmeldingGruppeById {

    @Autowired
    private SkdEndringsmeldingGruppeRepository repository;

    public void execute(Long id) {
        repository.deleteById(id);
    }
}
