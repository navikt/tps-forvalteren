package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Service
public class DummyAdresseService {

    public static final String UTEN_FAST_BOSTED = "UTEN FAST BOSTED";
    public static final String SPSF_ADR = "SOT6";

    private static final String GATEADRESSE = "SANNERGATA";
    private static final String HUSNR = "2";
    private static final String POSTNR = "0557";
    private static final String GATEKODE = "16188";
    private static final String KOMMUNENR = "0301";

    private static final String ADRESSE_1 = SPSF_ADR;
    private static final String ADRESSE_2 = "POSTBOKS 2094 VIKA";
    private static final String ADRESSE_3 = "0125 OSLO";
    private static final String POST_LAND = "000";

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    public Adresse createDummyBoAdresse(Person person) {

        Gateadresse gateadresse =
                Gateadresse.builder()
                        .husnummer(HUSNR)
                        .gatekode(GATEKODE)
                        .adresse(GATEADRESSE)
                        .build();
        gateadresse.setPostnr(POSTNR);
        gateadresse.setKommunenr(KOMMUNENR);
        gateadresse.setPerson(person);
        gateadresse.setFlyttedato(hentDatoFraIdentService.extract(person.getIdent()));

        return gateadresse;
    }

    public Postadresse createDummyPostAdresse(Person person) {

        return Postadresse.builder()
                .postLinje1(ADRESSE_1)
                .postLinje2(ADRESSE_2)
                .postLinje3(ADRESSE_3)
                .postLand(POST_LAND)
                .person(person)
                .build();
    }

    public Adresse createAdresseUfb(Person person) {

        Gateadresse gateadresse = Gateadresse.builder()
                .adresse(UTEN_FAST_BOSTED)
                .build();
        gateadresse.setKommunenr(nonNull(person.getBoadresse()) ?
                nullcheckSetDefaultValue(person.getBoadresse().getKommunenr(), KOMMUNENR) : KOMMUNENR);
        gateadresse.setPerson(person);
        gateadresse.setFlyttedato(hentDatoFraIdentService.extract(person.getIdent()));

        return gateadresse;
    }
}
