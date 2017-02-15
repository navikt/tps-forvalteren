package no.nav.tps.vedlikehold.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.tps.Response;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;

import javax.jms.JMSException;

/**
 * Created by F148888 on 21.10.2016.
 */

public interface TpsRequestService {
    Response executeServiceRutineRequest(TpsServiceRoutineRequest request, TpsServiceRoutineDefinition serviceRoutine, TpsRequestContext context) throws Exception;
    void executeSkdMeldingRequest(String skdMelding, TpsRequestContext context) throws JMSException;
}
