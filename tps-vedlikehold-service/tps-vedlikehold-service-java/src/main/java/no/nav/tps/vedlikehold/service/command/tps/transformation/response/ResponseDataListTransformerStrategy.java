package no.nav.tps.vedlikehold.service.command.tps.transformation.response;

import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.Transformer;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseDataListTransformer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ResponseDataListTransformerStrategy implements ResponseTransformStrategy {


    @Override
    public void execute(Response response, Transformer transformer) {

        ResponseDataListTransformer t = (ResponseDataListTransformer) transformer;

        String xmlElement = t.getXmlElement();
        String xmlElementSingleResource = t.getXmlElementSingleResource();
        String xmlElementTotalHits = t.getXmlElementTotalHits();

        String xml = extractContentInXmlElement(response.getRawXml(), xmlElement, "");
        String totalHits = extractContentInXmlElement(xml, xmlElementTotalHits, "0");
        List<String> allResults = extractAllContentInXmlElement(xml, xmlElementSingleResource);

        response.setDataXmls(allResults);
        response.setTotalHits(Integer.parseInt(totalHits));
    }

    private List<String> extractAllContentInXmlElement(String xml, String xmlElement) {
        String pattern = "<"+xmlElement+">(.+?)</"+xmlElement+">";
        Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(xml);
        List<String> allResultsAsXml = new ArrayList<>();
        while(matcher.find()) {
            allResultsAsXml.add(matcher.group(1));
        }
        return allResultsAsXml;
    }

    private String extractContentInXmlElement(String xml, String xmlElement, String defaultValue) {
        String pattern = "<"+xmlElement+">(.+?)</"+xmlElement+">";
        Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(xml);
        return matcher.find() ? matcher.group(1) : defaultValue;
    }



    @Override
    public boolean isSupported(Object o) {
        return o instanceof ResponseDataListTransformer;
    }
}
