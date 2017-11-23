package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;

public interface SkdEndringsmeldingLoggRepository extends Repository<SkdEndringsmeldingLogg, Long> {

    void save(SkdEndringsmeldingLogg skdEndringsmeldingLogg);

    List<SkdEndringsmeldingLogg> findAllByMeldingsgruppeId(Long gruppeId);
}
