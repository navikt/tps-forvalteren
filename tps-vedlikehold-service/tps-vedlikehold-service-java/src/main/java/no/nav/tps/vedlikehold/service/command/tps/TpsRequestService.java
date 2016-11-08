package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;

/**
 * Created by F148888 on 21.10.2016.
 */

public interface TpsRequestService {
    Response executeServiceRutineRequest(TpsRequestServiceRoutine request, TpsServiceRoutine serviceRoutine) throws Exception;
}
