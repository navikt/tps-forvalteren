package no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Peter Fløgstad on 17.10.2016.
 */

@Component
public class RsTpsResponseMappingUtils {

    @Autowired
    private ObjectMapper objectMapper;

    public TpsServiceRoutineResponse convertToTpsServiceRutineResponse(Response r) throws IOException {
        TpsServiceRoutineResponse response = new TpsServiceRoutineResponse();

        response.setXml(r.getRawXml());

        JSONObject jsonObject = new JSONObject();
        if (r.getDataXmls() != null) {
            List<JSONObject> data =  r.getDataXmls()
                    .stream()
                    .map(XML::toJSONObject)
                    .collect(Collectors.toList());

            jsonObject.put("data", data);
        }

        if (r.getTotalHits() != null) {
            jsonObject.put("antallTotalt", r.getTotalHits());
        }
        jsonObject.put("status", new JSONObject(r.getStatus()));

        Map data = objectMapper.readValue(jsonObject.toString(), Map.class);
        response.setResponse(data);

        return response;
    }

}
