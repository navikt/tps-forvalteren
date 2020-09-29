package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer.DEFAULT_LES_TIMEOUT;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.TpsRequestService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;

@Service
public class TpsRequestSender {

    @Autowired
    private FindServiceRoutineByName findServiceRoutineByName;

    @Autowired
    private RsTpsResponseMappingUtils rsTpsResponseMappingUtils;

    @Autowired
    private TpsRequestService tpsRequestService;

    @SneakyThrows
    public TpsServiceRoutineResponse sendTpsRequest(TpsServiceRoutineRequest request, TpsRequestContext context, long timeout) {
            Optional<TpsServiceRoutineDefinitionRequest> serviceRoutine = findServiceRoutineByName.execute(request.getServiceRutinenavn());
            if (serviceRoutine.isPresent()) {
                Response response = tpsRequestService.executeServiceRutineRequest(request, serviceRoutine.get(), context, timeout);
                return rsTpsResponseMappingUtils.convertToTpsServiceRutineResponse(response);
            }
            return null;
    }

    public TpsServiceRoutineResponse sendTpsRequest(TpsServiceRoutineRequest request, TpsRequestContext context) {
        return sendTpsRequest(request, context, DEFAULT_LES_TIMEOUT);
    }
}
