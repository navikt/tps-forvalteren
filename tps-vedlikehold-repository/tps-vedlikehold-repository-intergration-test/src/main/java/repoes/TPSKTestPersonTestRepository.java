package repoes;

import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Peter Fløgstad on 14.02.2017.
 */

public interface TPSKTestPersonTestRepository extends CrudRepository<TPSKTestPerson, Long> {
}
