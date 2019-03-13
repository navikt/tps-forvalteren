package no.nav.tps.forvalteren.service.command.testdata;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.UFB;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseUtil.createDummyAdresse;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.UfbAdresseUtil.UTEN_FAST_BOSTED;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.UfbAdresseUtil.createAdresseUfb;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Service
public class UpdateAdresseOgSpesregForUfb {

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    public Person updateAttributesForUfb(Person updatedPerson, Person storedPerson) {

        if (isUtenFastBobel(updatedPerson) && !isUtenFastBobel(storedPerson)) {

            updatedPerson.setBoadresse(createAdresseUfb(
                    nonNull(updatedPerson.getBoadresse()) ? updatedPerson.getBoadresse().getKommunenr() : null));
            updatedPerson.setSpesreg(nullcheckSetDefaultValue(updatedPerson.getSpesreg(), UFB.name()));
            updatedPerson.setSpesregDato(nullcheckSetDefaultValue(updatedPerson.getSpesregDato(), hentDatoFraIdentService.extract(updatedPerson.getIdent())));

        } else if (!isUtenFastBobel(updatedPerson) && isUtenFastBobel(storedPerson)) {

            if (isNull(updatedPerson.getBoadresse()) || isAdresseUtenFastBosted(updatedPerson)) {
                updatedPerson.setBoadresse(createDummyAdresse());
            }
            updatedPerson.setSpesregDato(null);
        }

        return updatedPerson;
    }

    private static boolean isAdresseUtenFastBosted(Person person) {
        return nonNull(person.getBoadresse()) &&
                person.getBoadresse() instanceof Gateadresse &&
                UTEN_FAST_BOSTED.equals(((Gateadresse) person.getBoadresse()).getAdresse());
    }

    private static boolean isUtenFastBobel(Person person) {
        return UFB.name().equals(person.getSpesreg()) || TRUE.equals(person.getUtenFastBopel());
    }
}
