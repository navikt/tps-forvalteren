package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;
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
            TpsServiceRoutineDefinition serviceRoutine = findServiceRoutineByName.execute(request.getServiceRutinenavn()).get();
            Response response = tpsRequestService.executeServiceRutineRequest(request, serviceRoutine, context);
            return rsTpsResponseMappingUtils.convertToTpsServiceRutineResponse(response);
        } catch (HttpUnauthorisedException ex){
            throw new HttpUnauthorisedException(ex, "api/v1/service/" + request.getServiceRutinenavn());
        } catch (Exception exception) {
            throw new HttpInternalServerErrorException(exception, "api/v1/service");
        }
    }
}
