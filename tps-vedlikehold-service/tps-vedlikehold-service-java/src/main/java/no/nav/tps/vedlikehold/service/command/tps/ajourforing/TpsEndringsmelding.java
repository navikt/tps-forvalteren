package no.nav.tps.vedlikehold.service.command.tps.ajourforing;

import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests.TpsRequestAjourforing;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.response.AjourforingResponse;

/**
 * Created by f148888 on 29.09.2016.
 */

@FunctionalInterface
public interface TpsEndringsmelding {
    AjourforingResponse execute(TpsRequestAjourforing requestAjourforing) throws Exception;
}
