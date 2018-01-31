package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.exceptions.DeathRowNotFoundException;

@Service
public class UpdateDeathRow {

    @Autowired
    private DeathRowRepository repository;

    public DeathRow execute(DeathRow deathRow) {
        DeathRow existingDeathRow = repository.findById(deathRow.getId());
        if (existingDeathRow == null) {
            throw new DeathRowNotFoundException("Fant ikke doedsmelding i repository");
        }
        return repository.save(deathRow);
    }
}
