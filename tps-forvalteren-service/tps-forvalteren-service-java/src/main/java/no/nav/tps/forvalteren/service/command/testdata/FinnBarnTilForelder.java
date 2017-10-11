package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinnBarnTilForelder {

    @Autowired
    private RelasjonRepository relasjonRepository;

    public List<Person> execute(Person forelder) {
        List<Relasjon> forelderBarnRelasjoner = relasjonRepository.findByPersonAndRelasjonTypeNavn(forelder, "BARN");
        return forelderBarnRelasjoner.stream()
                .map(Relasjon::getPersonRelasjonMed)
                .collect(Collectors.toList());
    }

}
