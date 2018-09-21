package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.data.repository.Repository;

public interface AdresseRepository extends Repository<Adresse, Long> {

    void deleteAllByPerson(Person person);

    Gateadresse getAdresseByPersonId(Long id);

    void deleteAll();
}
