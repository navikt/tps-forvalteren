package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.data.repository.CrudRepository;

public interface RelasjonTestRepository extends CrudRepository<Person, Long> {

}
