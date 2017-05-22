package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonTestRepository extends CrudRepository<Person, Long>{
}
