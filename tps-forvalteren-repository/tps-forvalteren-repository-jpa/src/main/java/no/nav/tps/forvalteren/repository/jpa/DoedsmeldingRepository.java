package no.nav.tps.forvalteren.repository.jpa;

import java.util.Collection;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;

public interface DoedsmeldingRepository extends CrudRepository<Doedsmelding, Long> {

    Doedsmelding findByPersonId(Long id);

    void deleteByPersonIdIn(Collection<Long> personIds);

    void deleteAll();
}
