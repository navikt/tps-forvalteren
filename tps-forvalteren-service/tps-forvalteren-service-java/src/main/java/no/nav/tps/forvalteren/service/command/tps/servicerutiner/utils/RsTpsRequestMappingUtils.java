package no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

@Component
@RequiredArgsConstructor
public class RsTpsRequestMappingUtils {

    private final ObjectMapper objectMapper;
    private final MessageProvider messageProvider;

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
            validateTpsRequestParameters(tpsServiceRoutineDefinitionRequest, map);
        }
        
        return (TpsServiceRoutineRequest)objectMapper.convertValue(map, requestClass);
    }
    
    public void validateTpsRequestParameters(TpsServiceRoutineDefinitionRequest tpsServiceRoutineDefinitionRequest, Map<String, Object> map) {
        List<String> requiredParameterNameList = tpsServiceRoutineDefinitionRequest.getRequiredParameterNameList();
        requiredParameterNameList.removeAll(map.keySet());
        if (!requiredParameterNameList.isEmpty()) {
            throw new TpsfFunctionalException(messageProvider.get("rest.service.request.exception.required", requiredParameterNameList));
        }
    }
}
