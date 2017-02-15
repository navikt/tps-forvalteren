package no.nav.tps.vedlikehold.domain.repository.jpa.repoes;

import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by Peter Fløgstad on 26.01.2017.
 */

public interface TPSKTestPersonRepository extends Repository<TPSKTestPerson, Long> {

    List<TPSKTestPerson> findAll();

    void save(TPSKTestPerson testPerson);
}