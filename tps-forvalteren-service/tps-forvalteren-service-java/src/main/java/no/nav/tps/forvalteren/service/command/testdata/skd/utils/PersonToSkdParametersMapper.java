package no.nav.tps.forvalteren.service.command.testdata.skd.utils;

import no.nav.tps.forvalteren.domain.jpa.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class PersonToSkdParametersMapper {


    //TODO gjør mer generisk
    public Map<String, String> execute(Person person){
        HashMap<String, String> skdParams = new HashMap<>();

        skdParams.put("T1-FODSELSDATO", person.getIdent().substring(0,6));
        skdParams.put("T1-PERSONNUMMER", person.getIdent().substring(6,11));
        skdParams.put("T1-FORNAVN", person.getFornavn());
        skdParams.put("T1-MELLOMNAVN", person.getMellomnavn());
        skdParams.put("T1-SLEKTSNAVN", person.getEtternavn());
        skdParams.put("T1-STATSBORGERSKAP", person.getStatsborgerskap());

        // Innvandring TODO Skal byttes ettervært
        skdParams.put("T1-INNVANDRET-FRA-LAND", "001");

        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = leggTilNuller(now.getMonthValue());
        String day = leggTilNuller(now.getDayOfMonth());
        String hour = leggTilNuller(now.getHour());
        String minute = leggTilNuller(now.getMinute());
        String second = leggTilNuller(now.getSecond());

        String yyyyMMdd = ""+year+month+day;
        String hhMMss = ""+hour+minute+second;

        skdParams.put("T1-MASKINDATO",yyyyMMdd);
        skdParams.put("T1-MASKINTID",hhMMss);

        skdParams.put("T1-REGDATO-ADR",yyyyMMdd);
        skdParams.put("T1-FLYTTEDATO-ADR",yyyyMMdd);

        // Default
        skdParams.put("T1-TRANSTYPE", "1");
        skdParams.put("T1-AARSAKSKODE", "02");
        skdParams.put("T1-STATUSKODE", "1");

        return skdParams;
    }

private String leggTilNuller(int tid){
        if(tid < 10){
            return "0" + tid;
        }
        return String.valueOf(tid);
    }

}
