package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;

public interface SkdEndringsmeldingRepository extends Repository<SkdEndringsmelding, Long> {

    SkdEndringsmelding findById(Long id);

    List<SkdEndringsmelding> findByIdIn(List<Long> ids);

    void save(SkdEndringsmelding skdEndringsmelding);

}
