package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Personmal;
import org.springframework.data.repository.Repository;

public interface PersonmalRepository extends Repository<Personmal, Long> {

    Personmal findById(Long id);

    List<Personmal> findAll();

    void save(Personmal personmal);

    void deleteById(Long id);

}
