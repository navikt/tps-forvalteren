package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.service.command.authorisation.strategy.DiskresjonskodeSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.EgenAnsattSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.ReadSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SecurityStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class DefaultSecurityStrategyService implements SecurityStrategyService {

    @Autowired
    private ReadSecurityStrategy readSecurityStrategy;

    @Autowired
    private DiskresjonskodeSecurityStrategy diskresjonskodeSecurityStrategy;

    @Autowired
    private EgenAnsattSecurityStrategy egenAnsattSecurityStrategy;

    @Override
    public ArrayList<SecurityStrategy> getSecurityStrategies(){
        ArrayList<SecurityStrategy> strategies = new ArrayList<>(Arrays.asList(diskresjonskodeSecurityStrategy,
                egenAnsattSecurityStrategy, readSecurityStrategy));
        return strategies;
    }
}
