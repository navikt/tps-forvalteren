package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;

public interface DoedsmeldingRepository extends Repository<Doedsmelding, Long> {

    Doedsmelding findByPersonId(Long id);

    Iterable<Doedsmelding> save(Iterable<Doedsmelding> doedsmeldinger);

    void deleteByPersonIdIn(List<Long> personIds);

    void deleteAll();
}
