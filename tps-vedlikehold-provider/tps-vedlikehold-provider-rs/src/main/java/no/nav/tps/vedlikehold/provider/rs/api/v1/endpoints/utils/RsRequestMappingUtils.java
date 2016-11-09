package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RsRequestMappingUtils {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    public <T> T convert(Map<String, Object> params, Class<T> type) {
        return objectMapper.convertValue(params, type);
    }

    public TpsRequestServiceRoutine convertToTpsRequestServiceRoutine(String serviceRutineNavn, JsonNode node) {
        Class<?> requestClass = getTpsServiceRutinerService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(serviceRutineNavn))
                .findFirst().get()
                .getJavaClass();

        return (TpsRequestServiceRoutine)objectMapper.convertValue(node, requestClass);
    }
}
