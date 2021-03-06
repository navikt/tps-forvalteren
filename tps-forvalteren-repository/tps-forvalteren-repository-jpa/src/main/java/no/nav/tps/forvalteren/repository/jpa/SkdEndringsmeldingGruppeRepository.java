package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;

import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;

public interface SkdEndringsmeldingGruppeRepository extends Repository<SkdEndringsmeldingGruppe, Long> {

    List<SkdEndringsmeldingGruppe> findAllByOrderByIdAsc();

    SkdEndringsmeldingGruppe findById(Long id);

    SkdEndringsmeldingGruppe save(SkdEndringsmeldingGruppe skdEndringsmeldingGruppe);

    void deleteById(Long id);

    void deleteAll();
}
