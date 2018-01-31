package no.nav.tps.forvalteren.repository.jpa;

import org.springframework.data.repository.Repository;

import java.util.List;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;

public interface DeathRowRepository extends Repository<DeathRow, Long> {

    DeathRow findById(Long id);

    DeathRow findByIdent(String ident);

    DeathRow save(DeathRow deathRow);

    void deleteById(Long id);

    List<DeathRow> findAll();
}
