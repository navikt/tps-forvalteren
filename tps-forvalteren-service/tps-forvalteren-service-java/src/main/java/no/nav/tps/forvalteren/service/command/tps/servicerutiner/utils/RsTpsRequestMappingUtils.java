package no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRutinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public TpsServiceRoutineRequest convertToTpsServiceRoutineRequest(String serviceRutineNavn, Map<String, Object> map) {
        return convertToTpsServiceRoutineRequest(serviceRutineNavn, map,false);
    }
    
    public TpsServiceRoutineRequest convertToTpsServiceRoutineRequest(String serviceRutineNavn, Map<String, Object> map, boolean validateRequestParameters) {
        TpsServiceRoutineDefinitionRequest tpsServiceRoutineDefinitionRequest = getTpsServiceRutinerService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(serviceRutineNavn))
                .findFirst().get();
        Class<?> requestClass = tpsServiceRoutineDefinitionRequest
                .getJavaClass();
    
        if (validateRequestParameters) {
            List<String> requiredParameterNameList = tpsServiceRoutineDefinitionRequest.getRequiredParameterNameList();
            requiredParameterNameList.removeAll(map.keySet());
            if (!requiredParameterNameList.isEmpty()) {
                throw new RuntimeException("Følgende påkrevde felter mangler:" + requiredParameterNameList.toString());
            }
        }
        
        return (TpsServiceRoutineRequest)objectMapper.convertValue(map, requestClass);
    }
}
