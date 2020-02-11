package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelasjonTestRepository extends CrudRepository<Relasjon, Long> {

    List<Relasjon> findAll();
}
