package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringVersionOfLocalDateTime;

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
        String yyyyMMdd = GetStringVersionOfLocalDateTime.yyyyMMdd(person.getRegdato());
        String hhMMss = GetStringVersionOfLocalDateTime.hhMMss(person.getRegdato());

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
                skdParams.put("gateGaard", ((Matrikkeladresse) boadresse).getGardsnr());
                skdParams.put("husBruk", ((Matrikkeladresse) boadresse).getBruksnr());
                skdParams.put("bokstavFestenr", ((Matrikkeladresse) boadresse).getFestenr());
                skdParams.put("undernr", ((Matrikkeladresse) boadresse).getUndernr());
                skdParams.put("adressenavn", ((Matrikkeladresse) boadresse).getMellomnavn());
            } else {
                skdParams.put("gateGaard", ((Gateadresse) boadresse).getGatekode());
                skdParams.put("husBruk", ((Gateadresse) boadresse).getHusnummer());
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

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_INNVANDRING);

        skdParams.put("innvandretFraLand", "001");
        skdParams.put("familienummer", "08096740140");
        skdParams.put("personkode", "1");
        skdParams.put("sivilstand", "1");
        skdParams.put("transtype", "1");
        skdParams.put("statuskode", "1");
    }
}
