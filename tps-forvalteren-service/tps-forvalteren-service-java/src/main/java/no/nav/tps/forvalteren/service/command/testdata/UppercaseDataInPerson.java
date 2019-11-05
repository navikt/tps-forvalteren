package no.nav.tps.forvalteren.service.command.testdata;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.nonNull;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;

@Service
public class UppercaseDataInPerson {

    public void execute(Person person) {
        person.setFornavn(person.getFornavn().toUpperCase());
        person.setEtternavn(person.getEtternavn().toUpperCase());
        person.setIdenttype(person.getIdenttype().toUpperCase());

        if (nonNull(person.getMellomnavn())) {
            person.setMellomnavn(person.getMellomnavn().toUpperCase());
        }

        postAdresseToUppercase(person);

        gateadresseToUppercase(person);
    }

    private void gateadresseToUppercase(Person person) {
        /* Gateadresse og Matrikkeladresse */
        Adresse boadresse = person.getBoadresse();
        if (nonNull(person.getBoadresse())) {
            if (boadresse instanceof Matrikkeladresse) {
                if (nonNull(((Matrikkeladresse) boadresse).getMellomnavn())) {
                    ((Matrikkeladresse) boadresse).setMellomnavn(((Matrikkeladresse) boadresse).getMellomnavn().toUpperCase());
                }
            } else {
                if (nonNull(((Gateadresse) boadresse).getAdresse())) {
                    ((Gateadresse) boadresse).setAdresse(((Gateadresse) boadresse).getAdresse().toUpperCase());
                }
                if (nonNull(((Gateadresse) boadresse).getHusnummer())) {
                    ((Gateadresse) boadresse).setHusnummer(((Gateadresse) boadresse).getHusnummer().toUpperCase());
                }
            }
        }
    }

    private void postAdresseToUppercase(Person person) {
        /* Postadresse */
        if (!person.getPostadresse().isEmpty()) {
            Postadresse postadresse = person.getPostadresse().get(0);

            if (nonNull(postadresse.getPostLinje1())) {
                postadresse.setPostLinje1(postadresse.getPostLinje1().toUpperCase());
            }
            if (nonNull(postadresse.getPostLinje2())) {
                postadresse.setPostLinje2(postadresse.getPostLinje2().toUpperCase());
            }
            if (nonNull(postadresse.getPostLinje3())) {
                postadresse.setPostLinje3(postadresse.getPostLinje3().toUpperCase());
            }
            person.setPostadresse(newArrayList(postadresse));
        }
    }
}
