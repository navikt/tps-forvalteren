package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;

public interface AdresseRepository extends CrudRepository<Adresse, Long> {

    @Modifying
    void deleteAllByPerson(Person person);

    @Modifying
    void deleteAll();

    Optional<List<Adresse>> findAdresseByPersonIdIn(List<Long> personIds);

    @Modifying
    int deleteByIdIn(List<Long> idents);
}
