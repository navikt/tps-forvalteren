package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.TpsServiceRutine;

import java.util.Collection;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public interface GetTpsServiceRutinerService {
    Collection<TpsServiceRutine> exectue();
}