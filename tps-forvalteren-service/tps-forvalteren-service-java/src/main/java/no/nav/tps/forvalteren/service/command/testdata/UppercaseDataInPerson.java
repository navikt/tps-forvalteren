package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UppercaseDataInPerson {

    public void execute(Person person){
        person.setFornavn(person.getFornavn().toUpperCase());
        person.setEtternavn(person.getEtternavn().toUpperCase());
        person.setIdenttype(person.getIdenttype().toUpperCase());

        if(person.getMellomnavn() != null){
            person.setMellomnavn(person.getMellomnavn().toUpperCase());
        }

        if(person.getPostadresse() != null && !person.getPostadresse().isEmpty()){
            Postadresse postadresse = person.getPostadresse().get(0);
            if(postadresse.getPostLinje1() != null){
                postadresse.setPostLinje1(postadresse.getPostLinje1().toUpperCase());
            }
            if(postadresse.getPostLinje2() != null){
                postadresse.setPostLinje2(postadresse.getPostLinje2().toUpperCase());
            }
            if(postadresse.getPostLinje3() != null){
                postadresse.setPostLinje3(postadresse.getPostLinje3().toUpperCase());
            }
            person.setPostadresse(Arrays.asList(postadresse));
        }


        Adresse boadresse = person.getBoadresse();
        if(person.getBoadresse() != null) {
            if (boadresse instanceof Matrikkeladresse) {
                if(((Matrikkeladresse) boadresse).getMellomnavn() != null) {
                    ((Matrikkeladresse) boadresse).setMellomnavn(((Matrikkeladresse) boadresse).getMellomnavn().toUpperCase());
                }
            } else {
                if( ((Gateadresse)boadresse).getAdresse() != null ){
                    ((Gateadresse)boadresse).setAdresse( ((Gateadresse) boadresse).getAdresse().toUpperCase());
                }
            }
        }
    }
}
