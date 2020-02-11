package no.nav.tps.forvalteren.repository.jpa;

import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;

public interface StatsborgerskapRepository extends CrudRepository<Statsborgerskap, Long> {

    @Modifying
    int deleteByIdIn(Set<Long> ids);
}
