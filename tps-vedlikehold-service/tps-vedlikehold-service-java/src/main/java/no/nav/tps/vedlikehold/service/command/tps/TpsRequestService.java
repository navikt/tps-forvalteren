package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsMessage;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsSystemInfo;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;

/**
 * Created by F148888 on 21.10.2016.
 */

public interface TpsRequestService {
    String executeServiceRutineRequest(TpsRequestServiceRoutine request, TpsMessage serviceRoutine) throws Exception;
    String executeEndringsmeldingRequest(TpsRequestEndringsmelding request, TpsSystemInfo tpsSystemInfo) throws Exception;
}
