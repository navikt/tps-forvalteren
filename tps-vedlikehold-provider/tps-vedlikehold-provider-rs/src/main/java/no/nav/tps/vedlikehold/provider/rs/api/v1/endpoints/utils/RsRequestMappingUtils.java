package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.utils;

import java.util.Map;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.GetTpsServiceRutinerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@Component
public class RsRequestMappingUtils {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GetTpsServiceRutinerService service;

    public <T> T convert(Map<String, Object> params, Class<T> type) {
        return objectMapper.convertValue(params, type);
    }

    public TpsRequest convertToTpsRequest(String serviceRutineNavn, JsonNode node) {
        Class<? extends TpsRequest> requestClass = service.exectue()
                .stream()
                .filter((e) -> e.getName().equalsIgnoreCase(serviceRutineNavn))
                .findFirst().get()
                .getJavaClass();

        return objectMapper.convertValue(node, requestClass);
    }
}
