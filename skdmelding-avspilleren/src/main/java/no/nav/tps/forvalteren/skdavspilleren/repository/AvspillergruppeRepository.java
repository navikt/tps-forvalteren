package no.nav.tps.forvalteren.skdavspilleren.repository;

import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;

public interface AvspillergruppeRepository extends CrudRepository<Avspillergruppe,Long> {
}
