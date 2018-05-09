package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;

@Service
public class BarnetranseSkdParameterStrategy {

    public Map<String, String> execute(Person forelder, List<Person> barn) {
        Map<String, String> skdParams = new HashMap<>();
        addSkdParametersExtractedFromForelder(skdParams, forelder);
        addSkdParametersExtractedFromBarn(skdParams, barn);
        addDefaultParameters(skdParams);
        return skdParams;
    }

    private void addSkdParametersExtractedFromForelder(Map<String, String> skdParams, Person forelder) {
        skdParams.put("fodselsnr", forelder.getIdent());
    }

    private void addSkdParametersExtractedFromBarn(Map<String, String> skdParams, List<Person> barn) {

        for (int counter = 0; counter < barn.size(); counter++) {
            Person currentBarn = barn.get(counter);
            skdParams.put("barnFodsdato" + (counter + 1), currentBarn.getIdent().substring(0, 6));
            skdParams.put("barnPersnr" + (counter + 1), currentBarn.getIdent().substring(6));
        }
    }

    private void addDefaultParameters(Map<String, String> skdParams) {
        String maskindato = ConvertDateToString.yyyyMMdd(LocalDateTime.now());
        String maskintid = ConvertDateToString.hhMMss(LocalDateTime.now());
        skdParams.put("maskindato", maskindato);
        skdParams.put("maskintid", maskintid);
        skdParams.put("transtype", "2");
        skdParams.put("aarsakskode", "98");
    }

}
