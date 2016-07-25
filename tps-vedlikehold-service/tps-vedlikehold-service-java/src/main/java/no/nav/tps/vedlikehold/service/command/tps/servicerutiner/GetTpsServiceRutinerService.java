package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import java.util.Collection;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.TpsServiceRutine;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@FunctionalInterface
public interface GetTpsServiceRutinerService {
    Collection<TpsServiceRutine> exectue();
}