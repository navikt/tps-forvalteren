package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Peter Fløgstad on 17.10.2016.
 */

@Component
public class TpsResponseMappingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpsResponseMappingUtils.class);

    @Autowired
    private ObjectMapper objectMapper;

    public ServiceRoutineResponse xmlResponseToServiceRoutineResponse(String responseXml, TpsRequestContext context) throws IOException{
        try {
            JSONObject jObject = XML.toJSONObject(responseXml);
            Object responseData = objectMapper.readValue(jObject.toString(), Map.class);          //TODO Map to custom object
            return new ServiceRoutineResponse(responseXml, responseData, context);
        } catch (IOException exception){
            LOGGER.error("Failed to convert TPS during XML marshalling with exception: {}", exception.toString());
            throw exception;
        }
    }
}
