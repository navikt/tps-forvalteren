package no.nav.tps.forvalteren.repository.jpa;

import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;

public interface InnvandretUtvandretRepository extends CrudRepository<InnvandretUtvandret, Long> {

    @Modifying
    int deleteByIdIn(Set<Long> ids);
}
