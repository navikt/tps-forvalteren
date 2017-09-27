package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;

import java.util.List;

public interface SkdCreateFamilierelasjoner {

    void execute(Person foreldre, List<Relasjon> foreldreBarnRelasjoner, List<String> environments);

}
