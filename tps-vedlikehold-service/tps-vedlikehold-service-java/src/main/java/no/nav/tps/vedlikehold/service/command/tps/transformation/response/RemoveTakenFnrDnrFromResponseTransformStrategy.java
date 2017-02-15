package no.nav.tps.vedlikehold.service.command.tps.transformation.response;

import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.RemoveTakenFnrFromResponseTransform;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Peter Fløgstad on 18.01.2017.
 */

@Component
public class RemoveTakenFnrDnrFromResponseTransformStrategy implements ResponseTransformStrategy{

    @Override
    public boolean isSupported(Object o) {
        return o instanceof RemoveTakenFnrFromResponseTransform;
    }

    @Override
    public void execute(Response response, Transformer transformer) {
        removeTakenFnrDnrFromResponse(response, (RemoveTakenFnrFromResponseTransform) transformer);
    }

    private void removeTakenFnrDnrFromResponse(Response response, RemoveTakenFnrFromResponseTransform transformer) {
        String fnrPattern = "<EFnr>.+?</EFnr>";
        String feilmldPattern = "<returMelding>S201005F</returMelding>";
        Matcher fnrMatcher = Pattern.compile(fnrPattern, Pattern.DOTALL).matcher(response.getRawXml());

        int fnrDnrRemoved = 0;

        while (fnrMatcher.find()){
           String fnrXml = fnrMatcher.group();
           Matcher feilmeldingMatcher = Pattern.compile(feilmldPattern, Pattern.DOTALL).matcher(fnrXml);
           if(!feilmeldingMatcher.find()){
               response.setRawXml(response.getRawXml().replace(fnrXml, ""));
               fnrDnrRemoved++;
           }
        }
        if(fnrDnrRemoved > 0){
            subtractNumberInXmlElement(transformer.getAntallFnrRequestTag(), fnrDnrRemoved, response);
        }
    }

    private void subtractNumberInXmlElement(String xmlElement, int valueToSubtract, Response response){
        String pattern = "(?:(?<=<" + xmlElement + ">))(\\d+?)(?=</" + xmlElement + ">)";
        Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(response.getRawXml());

        if (matcher.find()) {
            int existingNumber = Integer.parseInt(matcher.group(1));
            String replacementNumber = Integer.toString(existingNumber - valueToSubtract);

            response.setRawXml(matcher.replaceFirst(replacementNumber));
        }
    }
}
