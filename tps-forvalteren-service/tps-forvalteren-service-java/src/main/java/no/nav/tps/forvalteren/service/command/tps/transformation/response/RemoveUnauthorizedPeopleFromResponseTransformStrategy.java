package no.nav.tps.forvalteren.service.command.tps.transformation.response;

import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.RemoveUnauthorizedPeopleFromResponseTransform;
import no.nav.tps.forvalteren.service.command.authorisation.ForbiddenCallHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RemoveUnauthorizedPeopleFromResponseTransformStrategy implements ResponseTransformStrategy {

    private static final String FODSELSNUMMER_PATTERN = "<fnr>(\\d{11})</fnr>";
    private static final String PERSON_PATTERN = "<enPersonRes>.+?</enPersonRes>";

    @Autowired
    private ForbiddenCallHandlerService forbiddenCallHandlerService;

    @Value("${tps.forvalteren.production-mode}")
    private boolean currentEnvironmentIsProd;

    @Override
    public boolean isSupported(Object o) {
        return o instanceof RemoveUnauthorizedPeopleFromResponseTransform;
    }

    @Override
    public void execute(Response response, Transformer transformer) {
        if(currentEnvironmentIsProd){
            removeUnauthorizedPeopleFromResponse(response, (RemoveUnauthorizedPeopleFromResponseTransform) transformer);
        }
    }

    private void removeUnauthorizedPeopleFromResponse(Response response, RemoveUnauthorizedPeopleFromResponseTransform transformer) {
        int numberOfFnrRemoved = 0;

        Matcher personMatcher = Pattern.compile(PERSON_PATTERN, Pattern.DOTALL).matcher(response.getRawXml());

        while (personMatcher.find()) {
            String personXml = personMatcher.group();

            Matcher fnrMatcher = Pattern.compile(FODSELSNUMMER_PATTERN, Pattern.DOTALL).matcher(personXml);
            if (fnrMatcher.find()) {
                String fnr = fnrMatcher.group(1);
                if (!forbiddenCallHandlerService.isAuthorisedToFetchPersonInfo(response.getServiceRoutine(), fnr)) {
                    response.setRawXml(response.getRawXml().replace(personXml, ""));
                    ++numberOfFnrRemoved;
                }
            }
        }
        if (numberOfFnrRemoved > 0) {
            subtractNumberInXmlElement(transformer.getTotalHitsXmlElement(), numberOfFnrRemoved, response);
            subtractNumberInXmlElement(transformer.getHitsInBufferXmlElement(), numberOfFnrRemoved, response);
        }
    }


    private void subtractNumberInXmlElement(String xmlElement, int numberToSubtract, Response response) {
        String pattern = "(?:(?<=<" + xmlElement + ">))(\\d+?)(?=</" + xmlElement + ">)";
        Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(response.getRawXml());

        if (matcher.find()) {
            int existingNumber = Integer.parseInt(matcher.group(1));
            String replacementNumber = Integer.toString(existingNumber - numberToSubtract);

            response.setRawXml(matcher.replaceFirst(replacementNumber));
        }
    }
}
