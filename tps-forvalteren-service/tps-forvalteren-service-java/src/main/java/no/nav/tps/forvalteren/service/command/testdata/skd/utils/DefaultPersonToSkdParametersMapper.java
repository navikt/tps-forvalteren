package no.nav.tps.forvalteren.service.command.testdata.skd.utils;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.config.SkdConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultPersonToSkdParametersMapper implements PersonToSkdParametersMapper{

    private static final String TILDELINGSKODE_FOR_ENDRING = "2";
    private static final String TILDELINGSKODE_FOR_OPPRETT = "1";
    private static final String AARSAKSKODE_FOR_INNVANDRING = "02";

    //TODO gjør mer generisk
    public Map<String, String> create(Person person){
        HashMap<String, String> skdParams = new HashMap<>();

        skdParams.put(SkdConstants.TILDELINGSKODE, TILDELINGSKODE_FOR_OPPRETT);

        addParameters(skdParams, person);

        return skdParams;
    }

    public Map<String, String> update(Person person){
        HashMap<String, String> skdParams = new HashMap<>();

        skdParams.put(SkdConstants.TILDELINGSKODE, TILDELINGSKODE_FOR_ENDRING);

        addParameters(skdParams, person);

        return skdParams;
    }

    private void addParameters(Map<String, String> skdParams, Person person){
        skdParams.put(SkdConstants.FODSELSDATO, person.getIdent().substring(0,6));
        skdParams.put(SkdConstants.PERSONNUMMER, person.getIdent().substring(6,11));
        skdParams.put(SkdConstants.FORNAVN, person.getFornavn());
        skdParams.put(SkdConstants.MELLOMNAVN, person.getMellomnavn());
        skdParams.put(SkdConstants.SLEKTSNAVN, person.getEtternavn());
        skdParams.put(SkdConstants.STATSBORGERSKAP, person.getStatsborgerskap());

        // Innvandring TODO Skal byttes ettervært
        skdParams.put("T1-INNVANDRET-FRA-LAND", "001");

        LocalDateTime regDato = person.getRegdato();
        String year = String.valueOf(regDato.getYear());
        String month = leggTilNuller(regDato.getMonthValue());
        String day = leggTilNuller(regDato.getDayOfMonth());
        String hour = leggTilNuller(regDato.getHour());
        String minute = leggTilNuller(regDato.getMinute());
        String second = leggTilNuller(regDato.getSecond());

        String yyyyMMdd = ""+year+month+day;
        String hhMMss = ""+hour+minute+second;

        skdParams.put("T1-MASKINDATO",yyyyMMdd);
        skdParams.put("T1-MASKINTID",hhMMss);

        skdParams.put("T1-REG-DATO", yyyyMMdd);

        skdParams.put("T1-REGDATO-ADR",yyyyMMdd);
        skdParams.put("T1-FLYTTEDATO-ADR",yyyyMMdd);

        skdParams.put("T1-FRA-LAND-REGDATO",yyyyMMdd);
        skdParams.put("T1-FRA-LAND-FLYTTEDATO",yyyyMMdd);

        skdParams.put("T1-REGDATO-FAMNR",yyyyMMdd);


        skdParams.put("T1-INNVANDRET-FRA-LAND" ,"612");

        skdParams.put("T1-FAMILIENUMMER","08096740140");

        skdParams.put("T1-PERSONKODE", "1");

        skdParams.put("T1-SIVILSTAND", "1");

        // Default
        skdParams.put("T1-TRANSTYPE", "1");
        skdParams.put("T1-AARSAKSKODE", AARSAKSKODE_FOR_INNVANDRING);
        skdParams.put("T1-STATUSKODE", "1");

    }

private String leggTilNuller(int tid){
        if(tid < 10){
            return "0" + tid;
        }
        return String.valueOf(tid);
    }

}
