package no.nav.tps.forvalteren.service.command.tps.transformation.response;

import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.RemoveTakenFnrFromResponseTransform;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class RemoveTakenFnrDnrFromResponseTransformStrategy implements ResponseTransformStrategy{

    private static final String fnrPattern = "<EFnr>.+?</EFnr>";
    private static final String feilmldPattern = "<returMelding>S201005F</returMelding>";


    @Override
    public boolean isSupported(Object o) {
        return o instanceof RemoveTakenFnrFromResponseTransform;
    }

    @Override
    public void execute(Response response, Transformer transformer) {
        fjernUtilgjengeligeIdenterFraResponse(response, (RemoveTakenFnrFromResponseTransform) transformer);
    }

    private void fjernUtilgjengeligeIdenterFraResponse(Response response, RemoveTakenFnrFromResponseTransform transformer) {
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
