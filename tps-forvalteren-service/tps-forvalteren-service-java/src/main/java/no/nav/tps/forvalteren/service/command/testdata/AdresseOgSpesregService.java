package no.nav.tps.forvalteren.service.command.testdata;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.SPSF;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.UFB;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService.SPSF_ADR;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Service
public class AdresseOgSpesregService {

    @Autowired
    private DummyAdresseService dummyAdresseService;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    public Person updateAdresseOgSpesregAttributes(Person person) {

        if (SPSF.name().equals(person.getSpesreg())) {
            if (person.getPostadresse().isEmpty()) {
                person.getPostadresse().add(dummyAdresseService.createDummyPostAdresse(person));
            }
            person.setBoadresse(null);
            person.setSpesregDato(nullcheckSetDefaultValue(person.getSpesregDato(), hentDatoFraIdentService.extract(person.getIdent())));

        } else if (isUtenFastBobel(person)) {

            person.setBoadresse(dummyAdresseService.createAdresseUfb(person));
            person.setSpesreg(nullcheckSetDefaultValue(person.getSpesreg(), UFB.name()));
            person.setSpesregDato(nullcheckSetDefaultValue(person.getSpesregDato(), hentDatoFraIdentService.extract(person.getIdent())));

        } else if (isNull(person.getBoadresse())) {
            person.setBoadresse(dummyAdresseService.createDummyBoAdresse(person));
            person.setSpesregDato(null);
            if (!person.getPostadresse().isEmpty() && SPSF_ADR.equals(person.getPostadresse().get(0).getPostLinje1())) {
                person.setPostadresse(null);
            }
        }

        person.getPostadresse().forEach(adresse -> adresse.setPerson(person));

        return person;
    }

    private static boolean isUtenFastBobel(Person person) {
        return (UFB.name().equals(person.getSpesreg()) || TRUE.equals(person.getUtenFastBopel())) && SPSF.name().equals(person.getSpesreg());
    }
}
