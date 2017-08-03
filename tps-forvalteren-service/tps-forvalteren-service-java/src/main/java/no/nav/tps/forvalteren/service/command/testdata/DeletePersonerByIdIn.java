package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeletePersonerByIdIn {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RelasjonRepository relasjonRepository;

    public void execute(List<Long> ids){
        List<Person> personer = personRepository.findByIdentIn(idsToStringIds(ids));
        for(Person person : personer){
            for(Relasjon relasjon : person.getRelasjoner()){
                relasjonRepository.deleteById(relasjon.getId());
            }
        }
        personRepository.deleteByIdIn(ids);
    }

    private List<String> idsToStringIds(List<Long> ids){
        List<String> idsString = new ArrayList<>();
        for(Long id : ids){
            idsString.add(id.toString());
        }
        return idsString;
    }
}
