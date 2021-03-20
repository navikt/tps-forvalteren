package no.nav.tps.forvalteren.repository.jpa;

import java.util.Set;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Vergemaal;

public interface VergemaalRepository extends Repository<Vergemaal, Long> {

    Vergemaal findById(long id);

    void save(Vergemaal vergemaal);

    void deleteById(Long id);

    int deleteByIdIn(Set<Long> ids);

    void deleteAll();
}
