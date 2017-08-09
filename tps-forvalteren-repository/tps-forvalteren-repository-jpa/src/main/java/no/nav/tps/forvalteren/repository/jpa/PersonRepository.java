package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface PersonRepository extends Repository<Person, Long> {

    List<Person> findAllByOrderByIdAsc();

    void deleteByIdIn(List<Long> ids);

    void save(Iterable<Person> personer);

    List<Person> findByIdentIn(List<String> identListe);


}
