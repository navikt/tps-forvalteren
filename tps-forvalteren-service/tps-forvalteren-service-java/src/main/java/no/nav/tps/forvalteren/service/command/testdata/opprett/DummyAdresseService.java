package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
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
    private static final String HUSNR = "6";
    private static final String POSTNR = "0557";
    private static final String GATEKODE = "16188";
    private static final String KOMMUNENR = "0301";

    private static final String ADRESSE_1 = SPSF_ADR;
    private static final String ADRESSE_2 = "POSTBOKS 2094 VIKA";
    private static final String ADRESSE_3 = "0125 OSLO";
    private static final String POST_LAND = "NOR";

    private static final String ADRESSE_1_UTLAND = "1KOLEJOWA 6/5";
    private static final String ADRESSE_2_UTLAND = "18-500 KOLNO";
    private static final String ADRESSE_3_UTLAND = "CAPITAL WEST 3000";
    private static final String POST_LAND_UTLAND = "POL";

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
        gateadresse.setFlyttedato(nonNull(person) && isNotBlank(person.getIdent()) ?
                hentDatoFraIdentService.extract(person.getIdent()) : LocalDateTime.now());

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

    public Postadresse createDummyPostAdresseUtland(Person person) {

        return Postadresse.builder()
                .postLinje1(ADRESSE_1_UTLAND)
                .postLinje2(ADRESSE_2_UTLAND)
                .postLinje3(ADRESSE_3_UTLAND)
                .postLand(isNotBlank(person.getInnvandretFraLand()) ? person.getInnvandretFraLand() : POST_LAND_UTLAND)
                .person(person)
                .build();
    }

    public Postadresse createPostAdresseUtvandret(Person person) {

        if (person.getPostadresse().isEmpty()) {
            return Postadresse.builder()
                    .postLinje1(ADRESSE_1_UTLAND)
                    .postLinje2(ADRESSE_2_UTLAND)
                    .postLand(person.getUtvandretTilLand())
                    .person(person)
                    .build();
        } else {
            return Postadresse.builder()
                    .postLinje1(person.getPostadresse().get(0).getPostLinje1())
                    .postLinje2(person.getPostadresse().get(0).getPostLinje2())
                    .postLinje3(person.getPostadresse().get(0).getPostLinje3())
                    .postLand(person.getPostadresse().get(0).getPostLand())
                    .person(person)
                    .build();
        }
    }

    public Adresse createAdresseUfb(Person person, Adresse adresse) {

        Gateadresse gateadresse = Gateadresse.builder()
                .adresse(UTEN_FAST_BOSTED)
                .build();
        gateadresse.setKommunenr(nonNull(adresse) ?
                nullcheckSetDefaultValue(adresse.getKommunenr(), KOMMUNENR) : KOMMUNENR);
        gateadresse.setPerson(person);
        gateadresse.setFlyttedato(hentDatoFraIdentService.extract(person.getIdent()));

        return gateadresse;
    }
}
