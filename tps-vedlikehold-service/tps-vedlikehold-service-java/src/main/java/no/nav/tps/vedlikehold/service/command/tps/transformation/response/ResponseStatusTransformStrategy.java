package no.nav.tps.vedlikehold.service.command.tps.transformation.response;

import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.ResponseStatus;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ResponseStatusTransformStrategy implements ResponseTransformStrategy {


    @Override
    public void execute(Response response, Transformer transformer) {
        String xmlElement = ((ResponseStatusTransformer) transformer).getXmlElement();
        String filteredXml = extractContentInXmlElement(response.getRawXml(), xmlElement);
        response.setStatus(resolveStatusFromXml(filteredXml));
    }


    private ResponseStatus resolveStatusFromXml(String xml) {
        ResponseStatus status = new ResponseStatus();
        status.setKode(extractContentInXmlElement(xml, "returStatus"));
        status.setMelding(extractContentInXmlElement(xml, "returMelding"));
        status.setUtfyllendeMelding(extractContentInXmlElement(xml, "utfyllendeMelding"));
        return status;
    }

    private String extractContentInXmlElement(String xml, String xmlElement) {
        String pattern = "<"+xmlElement+">(.+?)</"+xmlElement+">";
        Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(xml);
        return matcher.find() ? matcher.group(1) : "";
    }

    @Override
    public boolean isSupported(Object o) {
        return o instanceof ResponseStatusTransformer;
    }
}
