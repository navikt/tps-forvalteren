package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;

@Service
public class SetAdresse {

    @Autowired
    private HusbokstavEncoder husbokstavEncoder;

    private static final Pattern HUSNUMMER_PATTERN = Pattern.compile("(\\d+)");
    private static final Pattern HUSBOKSTAV_PATTERN = Pattern.compile("([A-ZÆØÅÁ])");

    public void execute(Map<String, String> skdParams, Person person) {

        /* Boadresse */
        Adresse boadresse = person.getBoadresse();
        if (person.getBoadresse() != null) {
            if (boadresse instanceof Matrikkeladresse) {
                skdParams.put("gateGaard", ((Matrikkeladresse) boadresse).getGardsnr());
                skdParams.put("husBruk", ((Matrikkeladresse) boadresse).getBruksnr());
                skdParams.put("bokstavFestenr", ((Matrikkeladresse) boadresse).getFestenr());
                skdParams.put("undernr", ((Matrikkeladresse) boadresse).getUndernr());
                skdParams.put("adressenavn", ((Matrikkeladresse) boadresse).getMellomnavn());
            } else {
                skdParams.put("gateGaard", ((Gateadresse) boadresse).getGatekode());
                addHusBrukAndBokstavFestenr(skdParams, (Gateadresse) boadresse);
                String adresse = ((Gateadresse) boadresse).getAdresse();
                if (adresse != null) {
                    int lengAdr = adresse.length() > 25 ? 25 : adresse.length();
                    skdParams.put("adressenavn", ((Gateadresse) boadresse).getAdresse().substring(0, lengAdr));
                }
            }
            skdParams.put("kommunenummer", boadresse.getKommunenr());
            skdParams.put("postnummer", boadresse.getPostnr());

            LocalDateTime flytteDato = boadresse.getFlyttedato();
            if (flytteDato != null) {
                skdParams.put("flyttedatoAdr", String.format("%04d%02d%02d", flytteDato.getYear(), flytteDato.getMonthValue(), flytteDato.getDayOfMonth()));
            }
            skdParams.put("adressetype", "O");
        }

        /* Postadresse */
        if (person.getPostadresse() != null && !person.getPostadresse().isEmpty()) {
            Postadresse postadresse = person.getPostadresse().get(0);
            skdParams.put(SkdConstants.POSTADRESSE_ADR_1, postadresse.getPostLinje1());
            skdParams.put(SkdConstants.POSTADRESSE_ADR_2, postadresse.getPostLinje2());
            skdParams.put(SkdConstants.POSTADRESSE_ADR_3, postadresse.getPostLinje3());
            skdParams.put(SkdConstants.POSTADRESSE_LAND, postadresse.getPostLand());
        }
    }

    private void addHusBrukAndBokstavFestenr(Map<String, String> skdParams, Gateadresse gateadresse) {
        if(gateadresse.getHusnummer() != null) {
            Matcher husbokstavMatcher = HUSBOKSTAV_PATTERN.matcher(gateadresse.getHusnummer());
            Matcher husnummerMatcher = HUSNUMMER_PATTERN.matcher(gateadresse.getHusnummer());

            if(husbokstavMatcher.find()) {
                skdParams.put("bokstavFestenr", husbokstavEncoder.encode(husbokstavMatcher.group(1)));
            }
            if(husnummerMatcher.find()) {
                skdParams.put("husBruk", husnummerMatcher.group(1));
            }
        }

    }
    
}
