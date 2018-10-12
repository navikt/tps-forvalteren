package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;

@Service
public class FindAllSkdEndringsmeldingGrupper {

    @Autowired
    private SkdEndringsmeldingGruppeRepository repository;

    public List<SkdEndringsmeldingGruppe> execute() {
        List<SkdEndringsmeldingGruppe> grupper = repository.findAllByOrderByIdAsc();

        grupper.forEach(gruppe -> gruppe.getSkdEndringsmeldinger().clear());
        return grupper;
    }
}