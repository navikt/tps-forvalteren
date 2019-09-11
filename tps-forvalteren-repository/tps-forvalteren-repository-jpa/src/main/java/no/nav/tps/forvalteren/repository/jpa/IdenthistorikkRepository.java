package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;

public interface IdenthistorikkRepository extends Repository<IdentHistorikk, Long> {

    IdentHistorikk findByPersonId(Long id);

    Iterable<Doedsmelding> save(Iterable<Doedsmelding> doedsmeldinger);

    void deleteByPersonIn(List<Long> personIds);

    void deleteAll();
}
