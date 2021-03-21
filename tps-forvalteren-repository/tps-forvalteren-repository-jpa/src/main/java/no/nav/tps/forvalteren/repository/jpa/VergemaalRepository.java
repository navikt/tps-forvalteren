package no.nav.tps.forvalteren.repository.jpa;

import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Vergemaal;

public interface VergemaalRepository extends Repository<Vergemaal, Long> {

    Vergemaal findById(long id);

    @Modifying
    void save(Vergemaal vergemaal);

    @Modifying
    void deleteById(Long id);

    @Modifying
    int deleteByIdIn(Set<Long> ids);

    @Modifying
    void deleteAll();

    @Modifying
    int deleteByVergeId(Long personId);
}
