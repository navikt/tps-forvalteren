package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService.enforceValidTpsDate;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Service
public class SetAdresseService {

    @Autowired
    private HusbokstavEncoder husbokstavEncoder;

    @Autowired
    private LandkodeEncoder landkodeEncoder;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    private static final Pattern HUSNUMMER_PATTERN = Pattern.compile("(\\d+)");
    private static final Pattern HUSBOKSTAV_PATTERN = Pattern.compile("([A-ZÆØÅÁ])");

    public void execute(SkdMeldingTrans1 skdMeldingTrans1, Person person) {

        /* Boadresse */
        Adresse boadresse = person.getBoadresse();
        if (nonNull(person.getBoadresse())) {
            if (boadresse instanceof Matrikkeladresse) {
                skdMeldingTrans1.setAdressetype("M");
                skdMeldingTrans1.setGateGaard(prepad(((Matrikkeladresse) boadresse).getGardsnr(), 5));
                skdMeldingTrans1.setHusBruk(prepad(((Matrikkeladresse) boadresse).getBruksnr(), 4));
                skdMeldingTrans1.setBokstavFestenr(prepad(((Matrikkeladresse) boadresse).getFestenr(), 4));
                skdMeldingTrans1.setUndernr(prepad(((Matrikkeladresse) boadresse).getUndernr(), 3));
                skdMeldingTrans1.setAdressenavn(((Matrikkeladresse) boadresse).getMellomnavn());
            } else {
                skdMeldingTrans1.setAdressetype("O");
                skdMeldingTrans1.setGateGaard(((Gateadresse) boadresse).getGatekode());
                addHusBrukAndBokstavFestenr(skdMeldingTrans1, (Gateadresse) boadresse);
                String adresse = ((Gateadresse) boadresse).getAdresse();
                if (nonNull(adresse)) {
                    int lengAdr = adresse.length() > 25 ? 25 : adresse.length();
                    skdMeldingTrans1.setAdressenavn(((Gateadresse) boadresse).getAdresse().substring(0, lengAdr));
                }
            }
            skdMeldingTrans1.setBolignr(boadresse.getBolignr());
            skdMeldingTrans1.setKommunenummer(boadresse.getKommunenr());
            skdMeldingTrans1.setPostnummer(boadresse.getPostnr());

            skdMeldingTrans1.setFlyttedatoAdr(ConvertDateToString.yyyyMMdd(
                    enforceValidTpsDate(nullcheckSetDefaultValue(boadresse.getFlyttedato(), hentDatoFraIdentService.extract(person.getIdent())))));
        } else {
            skdMeldingTrans1.setFlyttedatoAdr(ConvertDateToString.yyyyMMdd(enforceValidTpsDate(hentDatoFraIdentService.extract(person.getIdent()))));
        }

        /* Postadresse */
        if (!person.getPostadresse().isEmpty()) {
            Postadresse postadresse = person.getPostadresse().get(0);
            skdMeldingTrans1.setPostadresse1(postadresse.getPostLinje1());
            skdMeldingTrans1.setPostadresse2(postadresse.getPostLinje2());
            skdMeldingTrans1.setPostadresse3(postadresse.getPostLinje3());
            skdMeldingTrans1.setPostadresseLand(landkodeEncoder.encode(postadresse.getPostLand()));
        }
    }

    private static String prepad(String value, int length) {
        String formatter = new StringBuilder().append("%1$").append(length).append('s').toString();
        return format(formatter, nonNull(value) ? value : '0').replace(' ', '0');
    }

    private void addHusBrukAndBokstavFestenr(SkdMeldingTrans1 skdMeldingTrans1, Gateadresse gateadresse) {
        if (nonNull(gateadresse.getHusnummer())) {
            Matcher husbokstavMatcher = HUSBOKSTAV_PATTERN.matcher(gateadresse.getHusnummer());
            Matcher husnummerMatcher = HUSNUMMER_PATTERN.matcher(gateadresse.getHusnummer());

            if (husbokstavMatcher.find()) {
                skdMeldingTrans1.setBokstavFestenr(husbokstavEncoder.encode(husbokstavMatcher.group(1)));
            }
            if (husnummerMatcher.find()) {
                skdMeldingTrans1.setHusBruk(husnummerMatcher.group(1));
            }
        }
    }
}
