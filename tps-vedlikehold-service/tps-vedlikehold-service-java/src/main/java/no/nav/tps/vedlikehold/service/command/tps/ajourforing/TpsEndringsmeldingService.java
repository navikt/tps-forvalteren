package no.nav.tps.vedlikehold.service.command.tps.ajourforing;

import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.response.EndringsmeldingResponse;

/**
 * Created by f148888 on 29.09.2016.
 */

@FunctionalInterface
public interface TpsEndringsmeldingService {
    EndringsmeldingResponse execute(TpsRequestEndringsmelding requestAjourforing) throws Exception;
}
