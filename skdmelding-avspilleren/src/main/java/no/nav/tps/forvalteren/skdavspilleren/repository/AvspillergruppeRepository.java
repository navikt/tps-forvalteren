package no.nav.tps.forvalteren.skdavspilleren.repository;

import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.skdavspilleren.domain.jpa.Avspillergruppe;

public interface AvspillergruppeRepository extends Repository<Avspillergruppe,Long> {
    Avspillergruppe findById(Long id);
}
