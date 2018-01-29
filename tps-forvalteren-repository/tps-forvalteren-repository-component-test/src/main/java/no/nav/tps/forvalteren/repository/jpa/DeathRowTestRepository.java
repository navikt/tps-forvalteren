package no.nav.tps.forvalteren.repository.jpa;

import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;

public interface DeathRowTestRepository extends CrudRepository<DeathRow, Long> {
}
