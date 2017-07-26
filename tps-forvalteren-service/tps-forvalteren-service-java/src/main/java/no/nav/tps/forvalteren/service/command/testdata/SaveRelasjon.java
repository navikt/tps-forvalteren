package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveRelasjon {

    @Autowired
    RelasjonRepository relasjonRepository;

    public void execute(Person person1, Person person2, RelasjonType relasjonType){
        Relasjon relasjon1 =  new Relasjon();
        relasjon1.setPerson(person1);
        relasjon1.setPersonRelasjonMed(person2);

        Relasjon relasjon2 =  new Relasjon();
        relasjon2.setPerson(person2);
        relasjon2.setPersonRelasjonMed(person1);

        relasjon1.setRelasjonTypeKode(relasjonType.getRelasjonTypeKode());
        relasjon2.setRelasjonTypeKode(relasjonType.getRelasjonTypeKode());

        for(Relasjon relasjon : person1.getRelasjoner()){
            if(relasjon.getRelasjonTypeKode() == relasjonType.getRelasjonTypeKode() &&
                    relasjon.getPersonRelasjonMed().getIdent().equalsIgnoreCase(person2.getIdent())){
                return;
            }
        }

        person1.getRelasjoner().add(relasjon1);
        person2.getRelasjoner().add(relasjon2);

        relasjonRepository.save(relasjon1);
        relasjonRepository.save(relasjon2);
    }
}
