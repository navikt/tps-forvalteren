package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelasjonForAndrePersonIEnRelasjonGetter {

    @Autowired
    private PersonRepository personRepository;

    public Relasjon execute(Relasjon relasjon){
        if(relasjon.getRelasjonTypeNavn().equalsIgnoreCase(RelasjonType.EKTEFELLE.name())){
            return getPersonBGiftemaalRelasjon(relasjon);
        } else {
            return getPersonBForeldreRelasjon(relasjon);
        }
    }

    private Relasjon getPersonBForeldreRelasjon(Relasjon relasjonForPersonA){
        Relasjon relasjonForPersonB = new Relasjon();
        Long personBiD = relasjonForPersonA.getPersonIdRelasjonMed();
        Long personA = relasjonForPersonA.getPersonId();

        relasjonForPersonB.setPersonId(personBiD);
        relasjonForPersonB.setPersonIdRelasjonMed(personA);

        if(relasjonForPersonA.getRelasjonTypeNavn().equalsIgnoreCase(RelasjonType.FAR.name()) ||
               relasjonForPersonA.getRelasjonTypeNavn().equalsIgnoreCase(RelasjonType.MOR.name())){

            relasjonForPersonB.setRelasjonTypeNavn(RelasjonType.BARN.name());

        } else {

            Person personBObj = personRepository.findById(personBiD);
            if(personBObj.getKjonn().equals('K')){
                relasjonForPersonB.setRelasjonTypeNavn(RelasjonType.MOR.name());
            } else {
                relasjonForPersonB.setRelasjonTypeNavn(RelasjonType.FAR.name());
            }

        }

        return relasjonForPersonB;
    }

    private Relasjon getPersonBGiftemaalRelasjon(Relasjon relasjonForPersonA){
        Relasjon relasjonForPersonB = new Relasjon();
        Long personBiD = relasjonForPersonA.getPersonIdRelasjonMed();
        Long personA = relasjonForPersonA.getPersonId();

        relasjonForPersonB.setPersonId(personBiD);
        relasjonForPersonB.setPersonIdRelasjonMed(personA);
        relasjonForPersonB.setRelasjonTypeNavn(relasjonForPersonA.getRelasjonTypeNavn());

        return relasjonForPersonB;
    }

}
