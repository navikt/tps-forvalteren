package no.nav.tps.forvalteren.repository.jpa;

import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Sivilstand;

public interface SivilstandRepository extends CrudRepository<Sivilstand, Long> {

    @Modifying
    int deleteByIdIn(Set<Long> ids);

    @Modifying
    int deleteByPersonRelasjonMedId(Long id);
}
