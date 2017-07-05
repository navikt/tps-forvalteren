package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class DeleteGruppeById {

    @Autowired
    private GruppeRepository gruppeRepository;

    @PreAuthorize("hasRole('0000-GA-TPSF-SKRIV')")
    public void execute(Long gruppeId){
        gruppeRepository.deleteById(gruppeId);
    }
}
