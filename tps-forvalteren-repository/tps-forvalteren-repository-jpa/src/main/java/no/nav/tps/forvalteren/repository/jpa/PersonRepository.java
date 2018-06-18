package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.data.repository.Repository;

public interface PersonRepository extends Repository<Person, Long> {

    List<Person> findAllByOrderByIdAsc();

    void deleteByIdIn(List<Long> ids);

    void save(Iterable<Person> personer);
    
    Person save(Person person);

    List<Person> findByIdentIn(List<String> identListe);

    List<Person> findByIdIn(List<Long> ids);

    Person findById(Long id);

    Person findByIdent(String ident);

    List<Person> findAll();

    void deleteByGruppeId(Long gruppeId);

    void deleteAll();
}
