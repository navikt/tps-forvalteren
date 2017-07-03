package no.nav.tps.forvalteren.service.command.testdata.skd.utils;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultPersonToSkdParametersForInnvandringMapper implements PersonToSkdParametersMapper {

    private static final String TILDELINGSKODE_FOR_ENDRING = "2";
    private static final String TILDELINGSKODE_FOR_OPPRETT = "1";
    private static final String AARSAKSKODE_FOR_INNVANDRING = "02";

    public Map<String, String> create(Person person) {
        HashMap<String, String> skdParams = new HashMap<>();

        skdParams.put(SkdConstants.TILDELINGSKODE, TILDELINGSKODE_FOR_OPPRETT);

        addSkdParametersExtractedFromPerson(skdParams, person);

        return skdParams;
    }

    public Map<String, String> update(Person person) {
        HashMap<String, String> skdParams = new HashMap<>();

        skdParams.put(SkdConstants.TILDELINGSKODE, TILDELINGSKODE_FOR_ENDRING);

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

        LocalDateTime regDato = person.getRegdato();
        String year = String.valueOf(regDato.getYear());
        String month = formaterDato(regDato.getMonthValue());
        String day = formaterDato(regDato.getDayOfMonth());
        String hour = formaterDato(regDato.getHour());
        String minute = formaterDato(regDato.getMinute());
        String second = formaterDato(regDato.getSecond());

        String yyyyMMdd = year + month + day;
        String hhMMss = hour + minute + second;

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

    private void addDefaultParam(Map<String, String> skdParams) {
        skdParams.put("T1-AARSAKSKODE", AARSAKSKODE_FOR_INNVANDRING);

        skdParams.put("T1-INNVANDRET-FRA-LAND", "001");
        skdParams.put("T1-FAMILIENUMMER", "08096740140");
        skdParams.put("T1-PERSONKODE", "1");
        skdParams.put("T1-SIVILSTAND", "1");
        skdParams.put("T1-TRANSTYPE", "1");
        skdParams.put("T1-STATUSKODE", "1");
    }

    private String formaterDato(int tid) {
        if (tid < 10) {
            return "0" + tid;
        }
        return String.valueOf(tid);
    }

}
