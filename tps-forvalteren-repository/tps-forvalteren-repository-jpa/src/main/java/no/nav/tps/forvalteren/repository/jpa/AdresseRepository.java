package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import org.springframework.data.repository.Repository;

public interface AdresseRepository extends Repository<Adresse, Long> {

    Adresse findAdresseByPersonId(Long id);

    void deleteById(Long id);

}
