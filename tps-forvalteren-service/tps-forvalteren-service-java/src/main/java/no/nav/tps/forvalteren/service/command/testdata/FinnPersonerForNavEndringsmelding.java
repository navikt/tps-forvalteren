package no.nav.tps.forvalteren.service.command.testdata;

import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.stereotype.Service;

@Service
public class FinnPersonerForNavEndringsmelding {

    public List<Person> execute(List<Person> personerSomIkkeEksistererITps){
        List<Person> personerSomSkalHaEndringsmelding = new ArrayList<>();
        personerSomIkkeEksistererITps.forEach(person -> {
            if(person.getTypeSikkerhetsTiltak()!=null){
                personerSomSkalHaEndringsmelding.add(person);
            } else if(person.getEgenAnsattDatoFom()!=null){
                personerSomSkalHaEndringsmelding.add(person);
            }
        });
        return personerSomSkalHaEndringsmelding;
    }
}
