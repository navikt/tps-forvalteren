package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmelding;

/**
 * Created by f148888 on 29.09.2016.
 */
@FunctionalInterface
public interface EndringsmeldingResolver {
    TpsEndringsmelding resolve();
}
