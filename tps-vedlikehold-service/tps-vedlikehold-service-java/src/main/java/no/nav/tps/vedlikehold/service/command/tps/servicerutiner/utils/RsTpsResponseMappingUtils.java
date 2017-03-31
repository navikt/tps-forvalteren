package no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.xml.XmlMapper;
import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpInternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class RsTpsResponseMappingUtils {

//    @Autowired
//    private ObjectMapper objectMapper;

//    @Autowired
//    private XmlMapper xmlMapper;

    public TpsServiceRoutineResponse convertToTpsServiceRutineResponse(Response response) throws IOException {
        TpsServiceRoutineResponse TpsServiceRutineResponse = new TpsServiceRoutineResponse();

        TpsServiceRutineResponse.setXml(response.getRawXml());

        XmlMapper mapser = new XmlMapper();
        Map<String, Object> responseMap = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        if(response.getDataXmls() != null){
           List<Map> data = response.getDataXmls()
                   .stream()
                   .map(xml -> {
                       try {
                           return mapser.readValue(xml, Map.class);
                       } catch (IOException e) {
                           //TODO Kast på en annen måte.
                           throw new HttpInternalServerErrorException(e, "api/v1/service");
                       }
                   })
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

}
