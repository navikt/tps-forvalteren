package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.RelasjonType;
import org.springframework.data.repository.Repository;

public interface RelasjonTypeRepository extends Repository<RelasjonType, Long> {

    void save(RelasjonType relasjon);

    RelasjonType findByName(String name);

    RelasjonType findById(int id);
}
