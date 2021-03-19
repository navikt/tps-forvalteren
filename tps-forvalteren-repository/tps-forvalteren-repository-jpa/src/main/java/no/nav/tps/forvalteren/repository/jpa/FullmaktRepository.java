package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Fullmakt;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface FullmaktRepository extends Repository<Fullmakt, Long> {

    Fullmakt findById(long id);

    void save(Fullmakt fullmakt);

    void deleteById(Long id);

    void deleteByIdIn(List<Long> ids);

    void deleteAll();
}
