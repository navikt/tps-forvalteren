package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RelasjonRepository extends Repository<Relasjon, Long> {

    Relasjon findById(long id);

    List<Relasjon> findByPersonId(Long id);

    List<Relasjon> findByPersonAndRelasjonTypeNavn(Person person, String relasjonTypeNavn);

    void deleteById(Long id);

    void deleteByPersonRelasjonMedIdIn(List<Long> personIds);

    void save(Relasjon relasjon);

}
