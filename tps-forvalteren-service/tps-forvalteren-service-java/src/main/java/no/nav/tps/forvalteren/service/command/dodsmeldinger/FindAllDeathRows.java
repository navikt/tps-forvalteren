package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;

@Service
public class FindAllDeathRows {

    @Autowired
    private DeathRowRepository repository;

    public List<DeathRow> execute() {
        return repository.findAll();
    }
}
