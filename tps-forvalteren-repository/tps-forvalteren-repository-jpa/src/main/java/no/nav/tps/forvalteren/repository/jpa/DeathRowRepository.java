package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import org.springframework.data.repository.Repository;

public interface DeathRowRepository extends Repository<DeathRow, Long> {

    DeathRow findById(Long id);

    DeathRow findByIdent(String ident);

    DeathRow save(DeathRow deathRow);

    void deleteById(Long id);

    void deleteAll();

    void deleteAllByMiljoe(String miljoe);

    List<DeathRow> findAll();

    List<DeathRow> findAllByHandling(String handling);

    DeathRow findByIdentAndMiljoe(String ident, String miljoe);
}

