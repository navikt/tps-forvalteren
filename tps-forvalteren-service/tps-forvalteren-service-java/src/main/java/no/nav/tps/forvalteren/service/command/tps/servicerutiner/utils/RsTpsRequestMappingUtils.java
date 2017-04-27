package no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RsTpsRequestMappingUtils {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    public <T> T convert(Map<String, Object> params, Class<T> type) {
        return objectMapper.convertValue(params, type);
    }

    public TpsServiceRoutineRequest convertToTpsServiceRoutineRequest(String serviceRutineNavn, JsonNode node) {
        Class<?> requestClass = getTpsServiceRutinerService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(serviceRutineNavn))
                .findFirst().get()
                .getJavaClass();

        return (TpsServiceRoutineRequest)objectMapper.convertValue(node, requestClass);
    }
}
