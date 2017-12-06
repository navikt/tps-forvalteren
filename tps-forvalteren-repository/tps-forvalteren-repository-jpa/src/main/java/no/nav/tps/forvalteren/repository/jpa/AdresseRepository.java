package no.nav.tps.forvalteren.repository.jpa;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import org.springframework.data.repository.Repository;

public interface AdresseRepository extends Repository<Adresse, Long> {

    List<Adresse> findAllAdresseByPersonId(Long id);

    void deleteAllById(List<Long> ids);

}
