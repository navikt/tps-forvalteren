package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;

public interface SkdEndringsmeldingRepository extends Repository<SkdEndringsmelding, Long> {
    
    SkdEndringsmelding findById(Long id);
    
    SkdEndringsmelding save(SkdEndringsmelding skdEndringsmelding);
    
    void deleteByIdIn(List<Long> ids);
    
    List<SkdEndringsmelding> findAllByGruppe(SkdEndringsmeldingGruppe gruppe);
    
    List<SkdEndringsmelding> findAllByGruppeOrderByIdAsc(SkdEndringsmeldingGruppe gruppe);
    
    void deleteAll();
}
