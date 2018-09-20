package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindPersonerSomSkalHaFoedselsmelding {

    @Autowired
    private RelasjonRepository relasjonRepository;

    public List<Person> execute(List<Person> personerIGruppe) {
        List<Relasjon> relasjonerMedBarn = new ArrayList<>();
        List<Person> barnSomSkalFodes = new ArrayList<>();
        personerIGruppe.stream().forEach(person ->
                relasjonerMedBarn.addAll(relasjonRepository.findByPersonAndRelasjonTypeNavn(person, "FOEDSEL"))
        );
        relasjonerMedBarn.stream().forEach(relasjon -> {
            Person person = relasjon.getPersonRelasjonMed();
            if (!barnSomSkalFodes.contains(person)) {
                barnSomSkalFodes.add(person);
            }
        });

        return barnSomSkalFodes;

//        return personerIGruppe.stream().filter(p -> containsFodsel(p.getRelasjoner())).collect(Collectors.toList());
    }

    private boolean containsFodsel(List<Relasjon> relasjoner) {
        for(Relasjon r : relasjoner){
            if(r.getRelasjonTypeNavn().equals(RelasjonType.FOEDSEL.getRelasjonTypeNavn())){
                return true;
            }
        }
        return false;
    }
}
