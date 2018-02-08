package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;

@Service
public class CreateDodsmelding {

    @Autowired
    private DeathRowRepository deathRowRepository;

    public void execute(DeathRow dodsmelding) {
        dodsmelding.setStatus("Ikke sendt");
        deathRowRepository.save(dodsmelding);
    }
}






