package no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class RsTpsResponseMappingUtils {

    private static final String HTML_TAG_WITH_CONTENT_PATTERN ="<(\\w+?)>(.*?)</\\1>";

    @Autowired
    private ObjectMapper objectMapper;


    public TpsServiceRoutineResponse convertToTpsServiceRutineResponse(Response response) throws IOException {
        TpsServiceRoutineResponse tpsServiceRutineResponse = new TpsServiceRoutineResponse();

        tpsServiceRutineResponse.setXml(response.getRawXml());

        JSONObject jsonObject = new JSONObject();
        if (response.getDataXmls() != null) {
            List<JSONObject> data = response.getDataXmls()
                    .stream()
                    .map(xml -> XML.toJSONObject(xml))
                    .collect(Collectors.toList());

            for(int i = 0; i < data.size(); i++){
                jsonObject.put("data"+(i+1), data.get(i));
            }
        }

        if (response.getTotalHits() != null) {
            jsonObject.put("antallTotalt", response.getTotalHits());
        }

        Map data = objectMapper.readValue(jsonObject.toString(), Map.class);
        tpsServiceRutineResponse.setResponse(data);

        return tpsServiceRutineResponse;
    }

    public TpsServiceRoutineResponse convertToTpsServiceRutineResponseRecursively(Response response) throws IOException {
        TpsServiceRoutineResponse tpsServiceRutineResponse = new TpsServiceRoutineResponse();

        tpsServiceRutineResponse.setXml(response.getRawXml());

        Map<String, Object> responseMap = new LinkedHashMap<>();


        if(response.getDataXmls() != null){
            List<Map> data = response.getDataXmls()
                    .stream()
                    .map(this::xmlToLinkedHashMap)
                    .collect(Collectors.toList());

            responseMap.put("data", data);
        }

        if(response.getTotalHits() != null){
            responseMap.put("antallTotalt", response.getTotalHits());
        }

        Map status = objectMapper.readValue(objectMapper.writeValueAsString(response.getStatus()), Map.class);
        responseMap.put("status", status);
        tpsServiceRutineResponse.setResponse(responseMap);

        return tpsServiceRutineResponse;
    }

    private Map<String, Object> xmlToLinkedHashMap(String xml){
        Map<String, Object> responseMap = new LinkedHashMap<>();
        Matcher tagMatcher = Pattern.compile(HTML_TAG_WITH_CONTENT_PATTERN, Pattern.DOTALL).matcher(xml);

        while(tagMatcher.find()){
            String tagContent = tagMatcher.group(2);
            String tagName = tagMatcher.group(1);
            if(tagHasNoChildren(tagContent)){
               responseMap.put(tagName, tagContent);
            } else {
                Object o = responseMap.get(tagName);
                if (o != null) {
                    responseMap.put(tagName, Arrays.asList(responseMap.get(tagName), xmlToLinkedHashMap(tagContent)));
                } else {
                    responseMap.put(tagName, xmlToLinkedHashMap(tagContent));
                }
            }
        }
        return responseMap;
    }

    private boolean tagHasNoChildren(String tagContent){
        return !tagContent.contains("<");
    }
}

