package no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class RsTpsResponseMappingUtils {

    private static final String htmlTagWithContentPattern ="<(\\w+?)>(.*?)</\\1>";

    @Autowired
    private ObjectMapper objectMapper;

    public TpsServiceRoutineResponse convertToTpsServiceRutineResponse(Response response) throws IOException {
        TpsServiceRoutineResponse TpsServiceRutineResponse = new TpsServiceRoutineResponse();

        TpsServiceRutineResponse.setXml(response.getRawXml());

        Map<String, Object> responseMap = new LinkedHashMap<>();

        if(response.getDataXmls() != null){
           List<Map> data = response.getDataXmls()
                   .stream()
                   .map(xml -> xmlToLinkedHashMap(xml))
                   .collect(Collectors.toList());

           responseMap.put("data", data);
        }

        if(response.getTotalHits() != null){
            responseMap.put("antallTotalt", response.getTotalHits());
        }

        Map status = objectMapper.readValue(objectMapper.writeValueAsString(response.getStatus()), Map.class);
        responseMap.put("status", status);
        TpsServiceRutineResponse.setResponse(responseMap);

        return TpsServiceRutineResponse;
    }

    private LinkedHashMap<String, Object> xmlToLinkedHashMap(String xml){
        LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
        Matcher tagMatcher = Pattern.compile(htmlTagWithContentPattern, Pattern.DOTALL).matcher(xml);

        while(tagMatcher.find()){
            String tagContent = tagMatcher.group(2);
            String tagName = tagMatcher.group(1);
            if(tagHasNoChildren(tagContent)){
                responseMap.put(tagName, tagContent);
            } else {
                responseMap.put(tagName, xmlToLinkedHashMap(tagContent));
            }
        }
        return responseMap;
    }

    private boolean tagHasNoChildren(String tagContent){
        return !tagContent.contains("<");
    }
}

