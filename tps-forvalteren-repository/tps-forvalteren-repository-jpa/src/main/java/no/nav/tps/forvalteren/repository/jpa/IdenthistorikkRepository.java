package no.nav.tps.forvalteren.repository.jpa;

import java.util.Collection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;

public interface IdenthistorikkRepository extends Repository<IdentHistorikk, Long> {

    IdentHistorikk findByPersonId(Long id);

    @Modifying
    Iterable<Doedsmelding> save(Iterable<Doedsmelding> doedsmeldinger);

    @Modifying
    int deleteByIdIn(Collection<Long> ids);

    @Modifying
    void deleteAll();

    @Modifying
    int deleteByAliasPersonId(Long id);
}
