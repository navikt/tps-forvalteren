package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.jpa.RelasjonType;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveGiftemaalRelasjon {

    @Autowired
    RelasjonRepository relasjonRepository;

    @Autowired
    RelasjonTypeRepository relasjonTypeRepository;

    public void execute(Person person1, Person person2){
        Relasjon relasjon1 =  new Relasjon();
        relasjon1.setPerson(person1);
        relasjon1.setPersonRelasjonMed(person2);

        Relasjon relasjon2 =  new Relasjon();
        relasjon2.setPerson(person2);
        relasjon2.setPersonRelasjonMed(person1);

        RelasjonType relasjonType;
        if(person1.getKjonn().equals(person2.getKjonn())){
            relasjonType = relasjonTypeRepository.findByName("Registrert partner");
        } else {
            relasjonType = relasjonTypeRepository.findByName("Gift");
        }
        relasjon1.setRelasjonType(relasjonType);
        relasjon2.setRelasjonType(relasjonType);

        person1.getRelasjoner().add(relasjon1);
        person2.getRelasjoner().add(relasjon2);

    }
}
