package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringversionOfLocalDateTime;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class InnvandringSkdParameterStrategy implements SkdParametersStrategy {

    private static final String TILDELINGSKODE_FOR_OPPRETT = "1";
    private static final String AARSAKSKODE_FOR_INNVANDRING = "02";

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof InnvandringSkdParametere;
    }

    @Override
    public Map<String, String> execute(Person person) {
        HashMap<String, String> skdParams = new HashMap<>();

        skdParams.put(SkdConstants.TILDELINGSKODE, TILDELINGSKODE_FOR_OPPRETT);

        addSkdParametersExtractedFromPerson(skdParams, person);

        return skdParams;
    }

    private void addSkdParametersExtractedFromPerson(Map<String, String> skdParams, Person person) {
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));
        skdParams.put(SkdConstants.FORNAVN, person.getFornavn());
        skdParams.put(SkdConstants.MELLOMNAVN, person.getMellomnavn());
        skdParams.put(SkdConstants.SLEKTSNAVN, person.getEtternavn());
        skdParams.put(SkdConstants.STATSBORGERSKAP, person.getStatsborgerskap());

        setAdresse(skdParams, person);
        String yyyyMMdd = GetStringversionOfLocalDateTime.yyyyMMdd(person.getRegdato());
        String hhMMss = GetStringversionOfLocalDateTime.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_ADR, yyyyMMdd);
        skdParams.put(SkdConstants.FLYTTEDATO_ADR, yyyyMMdd);
        skdParams.put(SkdConstants.FRA_LAND_REGDATO, yyyyMMdd);
        skdParams.put(SkdConstants.FRA_LAND_FLYTTEDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_FAM_NR, yyyyMMdd);

        addDefaultParam(skdParams);
    }

    private void setAdresse(Map<String, String> skdParams, Person person) {

        /* Boadresse */
        Adresse boadresse = person.getBoadresse();
        if (person.getBoadresse() != null) {
            if (boadresse instanceof Matrikkeladresse) {
                skdParams.put("T1-GATE-GAARD", ((Matrikkeladresse) boadresse).getGardsnr());
                skdParams.put("T1-HUS-BRUK", ((Matrikkeladresse) boadresse).getBruksnr());
                skdParams.put("T1-BOKSTAV-FESTENR", ((Matrikkeladresse) boadresse).getFestenr());
                skdParams.put("T1-UNDERNR", ((Matrikkeladresse) boadresse).getUndernr());
                skdParams.put("T1-ADRESSENAVN", ((Matrikkeladresse) boadresse).getMellomnavn());
            } else {
                skdParams.put("T1-GATE-GAARD", ((Gateadresse) boadresse).getGatekode());
                skdParams.put("T1-HUS-BRUK", ((Gateadresse) boadresse).getHusnummer());
                String adresse = ((Gateadresse) boadresse).getAdresse();
                if (adresse != null) {
                    int lengAdr = adresse.length() > 25 ? 25 : adresse.length();
                    skdParams.put("T1-ADRESSENAVN", ((Gateadresse) boadresse).getAdresse().substring(0, lengAdr));
                }
            }
            skdParams.put("T1-KOMMUNENUMMER", boadresse.getKommunenr());
            skdParams.put("T1-POSTNUMMER", boadresse.getPostnr());

            LocalDateTime flytteDato = boadresse.getFlyttedato();
            if (flytteDato != null) {
                skdParams.put("T1-FLYTTEDATO-ADR", String.format("%04d%02d%02d", flytteDato.getYear(), flytteDato.getMonthValue(), flytteDato.getDayOfMonth()));
            }
            skdParams.put("T1-ADRESSETYPE", "O");
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

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put("T1-AARSAKSKODE", AARSAKSKODE_FOR_INNVANDRING);

        skdParams.put("T1-INNVANDRET-FRA-LAND", "001");
        skdParams.put("T1-FAMILIENUMMER", "08096740140");
        skdParams.put("T1-PERSONKODE", "1");
        skdParams.put("T1-SIVILSTAND", "1");
        skdParams.put("T1-TRANSTYPE", "1");
        skdParams.put("T1-STATUSKODE", "1");
    }
}
