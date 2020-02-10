package no.nav.tps.forvalteren.repository.jpa;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findAllByOrderByIdAsc();

    @Modifying
    int deleteByIdIn(Collection<Long> ids);

    List<Person> findByIdentIn(Collection<String> identListe);

    List<Person> findByIdIn(List<Long> ids);

    Person findByIdent(String ident);

    List<Person> findAll();

    void deleteByGruppeId(Long gruppeId);

    void deleteAll();
}
