package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.SkdParametersStrategy;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.SetAdresse;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.StatsborgerskapEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.SIVILSTAND;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.TRANSTYPE;
import static no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants.TRANSTYPE_1;

public abstract class InnvandringSkdParameterStrategy implements SkdParametersStrategy {

    @Autowired
    private StatsborgerskapEncoder statsborgerskapEncoder;

    @Autowired
    private SetAdresse setAdresse;

    private static final String AARSAKSKODE_FOR_INNVANDRING = "02";

    @Override
    public Map<String, String> execute(Person person) {
        String tildelingskodeForInnvandirngsmelding = hentTildelingskode();

        HashMap<String, String> skdParams = new HashMap<>();
        skdParams.put(SkdConstants.TILDELINGSKODE, tildelingskodeForInnvandirngsmelding);

        addSkdParametersExtractedFromPerson(skdParams, person);
        addDefaultParam(skdParams);

        return skdParams;
    }

    private void addSkdParametersExtractedFromPerson(Map<String, String> skdParams, Person person) {
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0, 6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6, 11));
        skdParams.put(SkdConstants.FORNAVN, person.getFornavn());
        skdParams.put(SkdConstants.MELLOMNAVN, person.getMellomnavn());
        skdParams.put(SkdConstants.SLEKTSNAVN, person.getEtternavn());
        skdParams.put(SkdConstants.STATSBORGERSKAP, statsborgerskapEncoder.encode(person.getStatsborgerskap()));

        String yyyyMMdd = ConvertDateToString.yyyyMMdd(person.getRegdato());
        String hhMMss = ConvertDateToString.hhMMss(person.getRegdato());

        skdParams.put(SkdConstants.MASKINTID, hhMMss);
        skdParams.put(SkdConstants.MASKINDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_ADR, yyyyMMdd);
        skdParams.put(SkdConstants.FLYTTEDATO_ADR, yyyyMMdd);
        skdParams.put(SkdConstants.FRA_LAND_REGDATO, yyyyMMdd);
        skdParams.put(SkdConstants.FRA_LAND_FLYTTEDATO, yyyyMMdd);
        skdParams.put(SkdConstants.REG_DATO_FAM_NR, yyyyMMdd);

        setAdresse.execute(skdParams, person);
        addSpesreg(skdParams, person);
    }

    private void addSpesreg(Map<String, String> skdParams, Person person) {
        if (person.getSpesreg() != null) {
            skdParams.put("spesRegType", person.getSpesreg());
        }
        LocalDateTime spesregDato = person.getSpesregDato();
        if (spesregDato != null) {
            skdParams.put("datoSpesRegType", String.format("%04d%02d%02d", spesregDato.getYear(), spesregDato.getMonthValue(), spesregDato.getDayOfMonth()));
        }
    }

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put(SkdConstants.AARSAKSKODE, AARSAKSKODE_FOR_INNVANDRING);

        skdParams.put("innvandretFraLand", "001");
        skdParams.put("familienummer", "08096740140");
        skdParams.put("personkode", "1");
        skdParams.put(SIVILSTAND, "1");
        skdParams.put(TRANSTYPE, TRANSTYPE_1);
        skdParams.put("statuskode", "1");
    }
    
}
