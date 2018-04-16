package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import org.springframework.data.repository.Repository;

public interface VergemaalRepository extends Repository<Vergemaal, Long> {

    Vergemaal findById(long id);

    List<Vergemaal> findAllByIdent(String ident);

    void deleteByIdIn(Set<Long> ids);

    void save(Vergemaal vergemaal);

    void deleteAll();
}
