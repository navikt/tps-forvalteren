package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import org.springframework.data.repository.Repository;

public interface DoedsmeldingRepository extends Repository<Doedsmelding, Long> {

    Doedsmelding findDoedsmeldingByPersonId(Long id);

    void save(Iterable<Doedsmelding> doedsmeldinger);

    void deleteById(Long id);
}
