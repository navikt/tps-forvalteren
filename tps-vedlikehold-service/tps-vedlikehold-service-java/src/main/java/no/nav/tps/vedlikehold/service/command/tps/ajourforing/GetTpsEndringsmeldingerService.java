package no.nav.tps.vedlikehold.service.command.tps.ajourforing;

import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmelding;

import java.util.List;

/**
 * Created by f148888 on 30.09.2016.
 */

@FunctionalInterface
public interface GetTpsEndringsmeldingerService {
    List<TpsEndringsmelding> execute();
}
