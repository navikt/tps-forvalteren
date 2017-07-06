package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SaveGruppe {

    @Autowired
    private GruppeRepository gruppeRepository;

    public Gruppe execute(Gruppe gruppe){
        return gruppeRepository.save(gruppe);
    }
}
