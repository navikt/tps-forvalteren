package no.nav.tps.forvalteren.repository.jpa;

import java.util.Set;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Fullmakt;

public interface FullmaktRepository extends Repository<Fullmakt, Long> {

    Fullmakt findById(long id);

    void save(Fullmakt fullmakt);

    void deleteById(Long id);

    int deleteByIdIn(Set<Long> ids);

    void deleteAll();
}
