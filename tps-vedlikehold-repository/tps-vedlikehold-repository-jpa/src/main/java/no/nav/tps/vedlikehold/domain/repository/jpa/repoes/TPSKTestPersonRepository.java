package no.nav.tps.vedlikehold.domain.repository.jpa.repoes;

import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Peter Fløgstad on 26.01.2017.
 */

public interface TPSKTestPersonRepository extends Repository<TPSKTestPerson, Long> {

    List<TPSKTestPerson> findAll();

    @Query("from TPSKTestPerson where PERSONNUMMER = :personnummer and FODSELSNUMMER = :fodsel")
    TPSKTestPerson findByIdentNummer(@Param("fodsel") String fodselsnummer, @Param("personnummer") String personnummer);

    void save(TPSKTestPerson testPerson);
}