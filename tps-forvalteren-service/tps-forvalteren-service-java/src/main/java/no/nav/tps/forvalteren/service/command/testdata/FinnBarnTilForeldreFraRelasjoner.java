package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinnBarnTilForeldreFraRelasjoner {

    public List<Person> execute(List<Relasjon> foreldreBarnRelasjoner) {
        return foreldreBarnRelasjoner.stream().map(Relasjon::getPersonRelasjonMed).collect(Collectors.toList());
    }

}
