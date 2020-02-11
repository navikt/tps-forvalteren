package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;

public interface DeathRowRepository extends Repository<DeathRow, Long> {

    DeathRow findById(Long id);

    DeathRow save(DeathRow deathRow);

    void deleteById(Long id);

    void deleteAll();

    void deleteAllByMiljoe(String miljoe);

    List<DeathRow> findAll();

    List<DeathRow> findAllByStatus(String status);

    DeathRow findByIdentAndMiljoe(String ident, String miljoe);
}

