package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteGruppeById {

    @Autowired
    private GruppeRepository gruppeRepository;

    public void execute(Long gruppeId){
        gruppeRepository.deleteById(gruppeId);
    }
}
