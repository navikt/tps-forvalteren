package no.nav.tps.forvalteren.repository.jpa;

import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Fullmakt;

public interface FullmaktRepository extends Repository<Fullmakt, Long> {

    Fullmakt findById(long id);

    @Modifying
    void save(Fullmakt fullmakt);

    @Modifying
    void deleteById(Long id);

    @Modifying
    int deleteByIdIn(Set<Long> ids);

    @Modifying
    void deleteAll();

    @Modifying
    int deleteByFullmektigId(Long id);
}
