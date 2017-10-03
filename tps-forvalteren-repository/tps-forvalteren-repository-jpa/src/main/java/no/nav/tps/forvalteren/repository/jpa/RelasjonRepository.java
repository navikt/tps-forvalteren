package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import java.util.Set;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

public interface RelasjonRepository extends Repository<Relasjon, Long> {

    Relasjon findById(long id);

    List<Relasjon> findByPersonId(Long id);

    List<Relasjon> findByPersonAndRelasjonTypeNavn(Person person, String relasjonTypeNavn);

    void deleteByIdIn(Set<Long> ids);

    void deleteByPersonRelasjonMedIdIn(List<Long> personIds);

    void save(Relasjon relasjon);

}
