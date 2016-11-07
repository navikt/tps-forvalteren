package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsSystemInfo;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;

/**
 * Created by F148888 on 21.10.2016.
 */

public interface TpsRequestService {
    Response executeServiceRutineRequest(TpsRequestServiceRoutine request, TpsServiceRoutine serviceRoutine) throws Exception;
//    String executeEndringsmeldingRequest(TpsRequestEndringsmelding request, TpsSystemInfo tpsSystemInfo) throws Exception;
}
