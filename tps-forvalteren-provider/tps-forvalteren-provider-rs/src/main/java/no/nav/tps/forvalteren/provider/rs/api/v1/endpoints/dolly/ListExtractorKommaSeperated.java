package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.dolly;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@Service
public class ListExtractorKommaSeperated {

    public List<String> extractIdenter(String kommaSepString){
        String regex = "[0-9]{11}";
        return filter(kommaSepString, regex, "ExtractIdent error: En av identene i stringen fyller ikke opp kravene til å være en ident");
    }

    public List<String> extractEnvironments(String kommaSepString){
        String regex = "\\w{1}\\d{1,2}";
        return filter(kommaSepString, regex, "Extract Env error: En av miljøene er i ugyldig format");
    }

    private List<String> filter(String input, String regex, String feilmelding){
        String[] inputSplitted = input.split(",");
        Pattern pattern = Pattern.compile(regex);
        for(String i : inputSplitted){
            if(!pattern.matcher(i).matches()){
                throw new TpsfFunctionalException(feilmelding);
            }
        }
        return newArrayList(inputSplitted);

    }
}
