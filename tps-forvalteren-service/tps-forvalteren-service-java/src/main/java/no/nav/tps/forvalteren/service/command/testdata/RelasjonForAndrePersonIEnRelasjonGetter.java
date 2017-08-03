package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import org.springframework.stereotype.Service;

@Service
public class RelasjonForAndrePersonIEnRelasjonGetter {

    public Relasjon execute(Relasjon relasjon){
        if(relasjon.getRelasjonTypeNavn().equalsIgnoreCase(RelasjonType.EKTEFELLE.name())){
            return getPersonBGiftemaalRelasjon(relasjon);
        } else {
            return getPersonBForeldreRelasjon(relasjon);
        }
    }

    private Relasjon getPersonBForeldreRelasjon(Relasjon relasjonForPersonA){
        Relasjon relasjonForPersonB = new Relasjon();
        Person personB = relasjonForPersonA.getPersonRelasjonMed();
        Person personA = relasjonForPersonA.getPerson();

        relasjonForPersonB.setPerson(personB);
        relasjonForPersonB.setPersonRelasjonMed(personA);

        if(relasjonForPersonA.getRelasjonTypeNavn().equalsIgnoreCase(RelasjonType.FAR.name()) ||
               relasjonForPersonA.getRelasjonTypeNavn().equalsIgnoreCase(RelasjonType.MOR.name())){

            relasjonForPersonB.setRelasjonTypeNavn(RelasjonType.BARN.name());

        } else {

            if(personB.getKjonn().equals('K')){
                relasjonForPersonB.setRelasjonTypeNavn(RelasjonType.MOR.name());
            } else {
                relasjonForPersonB.setRelasjonTypeNavn(RelasjonType.FAR.name());
            }

        }

        return relasjonForPersonB;
    }

    private Relasjon getPersonBGiftemaalRelasjon(Relasjon relasjonPersonA){
        Relasjon relasjonPersonB = new Relasjon();
        Person personB = relasjonPersonA.getPersonRelasjonMed();
        Person personA = relasjonPersonA.getPerson();

        relasjonPersonB.setPerson(personB);
        relasjonPersonB.setPersonRelasjonMed(personA);
        relasjonPersonB.setRelasjonTypeNavn(relasjonPersonA.getRelasjonTypeNavn());

        return relasjonPersonB;
    }

}
