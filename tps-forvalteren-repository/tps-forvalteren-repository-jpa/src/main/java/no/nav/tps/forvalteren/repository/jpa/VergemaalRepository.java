package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import org.springframework.data.repository.Repository;

public interface VergemaalRepository extends Repository<Vergemaal, Long> {

    Vergemaal findById(long id);

    List<Vergemaal> findAllByIdent(String ident);

    Vergemaal findBySaksidAndInternVergeId(String saksId, String internVergeId);

    void save(Vergemaal vergemaal);

    void deleteById(Long id);

    void deleteAll();
}
