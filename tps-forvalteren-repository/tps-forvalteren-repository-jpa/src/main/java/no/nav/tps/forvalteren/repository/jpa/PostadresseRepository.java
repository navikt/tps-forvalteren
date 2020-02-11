package no.nav.tps.forvalteren.repository.jpa;

import org.springframework.data.repository.Repository;

import no.nav.tps.forvalteren.domain.jpa.Postadresse;

public interface PostadresseRepository extends Repository<Postadresse, Long> {

    void deletePostadresseById(Long id);
}