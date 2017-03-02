package no.nav.tps.vedlikehold.domain.repository.jpa.repoes;


import no.nav.tps.vedlikehold.domain.service.jpa.TestGruppe;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by Peter Fløgstad on 16.02.2017.
 */
public interface TestGruppeRepository extends Repository<TestGruppe, Long> {

    List<TestGruppe> findAll();

    List<TestGruppe> findByOwnerID(String ownerId);

    void save(TestGruppe testGruppe);

    // findTestgruppByPersonId?

}
