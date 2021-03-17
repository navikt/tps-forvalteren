package no.nav.tps.forvalteren.repository.jpa;

import java.util.Set;
import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Postadresse;

public interface PostadresseRepository extends Repository<Postadresse, Long> {

    void deleteById(Long id);

    int deleteByIdIn(Set<Long> id);
}