package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

public interface RelasjonRepository extends CrudRepository<Relasjon, Long> {

    Relasjon findById(long id);

    List<Relasjon> findByPersonId(Long id);

    List<Relasjon> findByPersonAndRelasjonTypeNavn(Person person, String relasjonTypeNavn);

    @Modifying
    void deleteByIdIn(Set<Long> ids);

    @Modifying
    void deleteByPersonRelasjonMedIdIn(Set<Long> personIds);
}
