package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SecurityStrategy;

import java.util.ArrayList;

/**
 * Created by F148888 on 08.11.2016.
 */

public interface SecurityStrategyService {
    ArrayList<SecurityStrategy> getSecurityStrategies();
}
