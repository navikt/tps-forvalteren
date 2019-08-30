package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Person;

public interface PersonRepository extends Repository<Person, Long> {

    List<Person> findAllByOrderByIdAsc();

    @Modifying
    int deleteByIdIn(List<Long> ids);

    List<Person> save(Iterable<Person> personer);

    Person save(Person person);

    List<Person> findByIdentIn(List<String> identListe);

    List<Person> findByIdIn(List<Long> ids);

    Person findById(Long id);

    Person findByIdent(String ident);

    List<Person> findAll();

    void deleteByGruppeId(Long gruppeId);

    void deleteAll();
}
