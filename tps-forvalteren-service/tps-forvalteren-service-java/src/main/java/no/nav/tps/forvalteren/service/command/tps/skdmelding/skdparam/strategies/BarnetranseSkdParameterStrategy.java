package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.GetStringVersionOfLocalDateTime;

@Service
public class BarnetranseSkdParameterStrategy {

    public Map<String, String> execute(Person foreldre, List<Person> barn) {
        Map<String, String> skdParams = new HashMap<>();
        addSkdParametersExtractedFromForeldre(skdParams, foreldre);
        addSkdParametersExtractedFromBarn(skdParams, barn);
        addDefaultParameters(skdParams);
        return skdParams;
    }

    private void addSkdParametersExtractedFromForeldre(Map<String, String> skdParams, Person foreldre) {
        skdParams.put("T2-FODSELSNR", foreldre.getIdent());
    }

    private void addSkdParametersExtractedFromBarn(Map<String, String> skdParams, List<Person> barn) {
        for (int counter = 0; counter < barn.size(); counter++) {
            Person currentBarn = barn.get(counter);
            skdParams.put("T2-BARN-FODSDATO" + counter, currentBarn.getIdent().substring(0, 6));
            skdParams.put("T2-BARN-PERSNR" + counter, currentBarn.getIdent().substring(6));
        }
    }

    private void addDefaultParameters(Map<String, String> skdParams) {
        String maskindato = GetStringVersionOfLocalDateTime.yyyyMMdd(LocalDateTime.now());
        String maskintid = GetStringVersionOfLocalDateTime.hhMMss(LocalDateTime.now());
        skdParams.put("T2-MASKINDATO", maskindato);
        skdParams.put("T2-MASKINTID", maskintid);
        skdParams.put("T2-TRANSTYPE", "2");
        skdParams.put("T2-AARSAKSKODE", "98");
    }

}
