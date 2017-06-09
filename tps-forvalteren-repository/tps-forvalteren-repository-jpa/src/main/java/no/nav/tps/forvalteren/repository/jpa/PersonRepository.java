package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.data.repository.Repository;

import javax.transaction.Transactional;
import java.util.List;

public interface PersonRepository extends Repository<Person, Long> {

    List<Person> findAllByOrderByIdAsc();

    @Transactional
    void deleteByIdIn(List<Long> ids);

    @Transactional
    void save(Iterable<Person> personer);

}
