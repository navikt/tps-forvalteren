package no.nav.tps.forvalteren.repository.jpa;

import java.util.Collection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;

public interface DoedsmeldingRepository extends CrudRepository<Doedsmelding, Long> {

    Doedsmelding findByPersonId(Long id);

    @Modifying
    void deleteByPersonIdIn(Collection<Long> personIds);

    @Modifying
    int deleteByPersonId(Long personId);

    @Modifying
    void deleteAll();
}
