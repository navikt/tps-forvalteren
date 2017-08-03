package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.tps.TpsRequestService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TpsRequestSender {

    @Autowired
    private FindServiceRoutineByName findServiceRoutineByName;

    @Autowired
    private RsTpsResponseMappingUtils rsTpsResponseMappingUtils;

    @Autowired
    private TpsRequestService tpsRequestService;

    public TpsServiceRoutineResponse sendTpsRequest(TpsServiceRoutineRequest request, TpsRequestContext context){
        try {
            TpsServiceRoutineDefinitionRequest serviceRoutine = findServiceRoutineByName.execute(request.getServiceRutinenavn()).get();
            Response response = tpsRequestService.executeServiceRutineRequest(request, serviceRoutine, context);
            return rsTpsResponseMappingUtils.convertToTpsServiceRutineResponse(response);

        } catch (HttpForbiddenException ex){
            throw new HttpForbiddenException(ex, "api/v1/service/" + request.getServiceRutinenavn());

        } catch (Exception exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");

        }

        //TODO kan kaste SOAP Exception ogsaa. Fra EgenAnsattConsumer.
    }
}
