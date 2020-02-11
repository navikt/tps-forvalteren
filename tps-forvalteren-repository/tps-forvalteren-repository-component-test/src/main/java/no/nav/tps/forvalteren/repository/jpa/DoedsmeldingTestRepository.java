package no.nav.tps.forvalteren.repository.jpa;

import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;

public interface DoedsmeldingTestRepository extends CrudRepository<Doedsmelding, Long> {
}
