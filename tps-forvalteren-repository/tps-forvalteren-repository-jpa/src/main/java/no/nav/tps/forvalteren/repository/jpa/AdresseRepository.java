package no.nav.tps.forvalteren.repository.jpa;

import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;

public interface AdresseRepository extends Repository<Adresse, Long> {

    void deleteAllByPerson(Person person);

    void save(Adresse adresse);

    void deleteAll();
}
