package no.nav.tps.forvalteren.repository.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

public interface RelasjonRepository extends CrudRepository<Relasjon, Long> {

    Relasjon findById(long id);

    List<Relasjon> findByPersonId(Long id);

    List<Relasjon> findByPersonAndRelasjonTypeNavn(Person person, String relasjonTypeNavn);

    Optional<List<Relasjon>> findByPersonRelasjonMedIdIn(Collection<Long> personIds);

    @Modifying
    void deleteByIdIn(Set<Long> ids);

    @Modifying
    void deleteByPersonRelasjonMedIdIn(List<Long> personIds);
}
