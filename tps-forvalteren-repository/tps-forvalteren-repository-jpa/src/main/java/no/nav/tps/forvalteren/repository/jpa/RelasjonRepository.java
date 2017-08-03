package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RelasjonRepository extends Repository<Relasjon, Long> {

    List<Relasjon> findAll();

    Relasjon findById(long id);

    void deleteById(Long id);

    void save(Relasjon relasjon);

}
