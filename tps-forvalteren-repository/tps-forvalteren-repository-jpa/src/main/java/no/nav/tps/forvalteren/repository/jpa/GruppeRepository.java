package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface GruppeRepository extends Repository<Gruppe, Long> {

    List<Gruppe> findAllByOrderByIdAsc();

    Gruppe findById(Long id);

    Gruppe findByNavn(String navn);

    Gruppe save(Gruppe gruppe);

    void deleteById(Long id);

    void deleteAll();

}
