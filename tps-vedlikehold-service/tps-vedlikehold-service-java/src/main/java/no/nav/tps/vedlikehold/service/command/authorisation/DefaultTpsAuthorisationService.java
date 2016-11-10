package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.RestSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SearchSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SecurityStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTpsAuthorisationService implements TpsAuthorisationService {

    @Autowired
    private List<SearchSecurityStrategy> searchPersonSecurityStrategies;

    @Autowired
    private List<RestSecurityStrategy> restSecurityStrategies;

    @Override
    public void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine, String environment, User user) {
        authorise(restSecurityStrategies, serviceRoutine, environment, user);
    }

    @Override
    public void authoriseFodselsnummer(TpsServiceRoutineDefinition serviceRoutine, String fnr, User user) {
        authorise(searchPersonSecurityStrategies, serviceRoutine, fnr, user);
    }

    private void authorise(List<? extends SecurityStrategy> securityStrategies, TpsServiceRoutineDefinition serviceRoutine, String param, User user) {
        for (AuthorisationStrategy authorisationStrategy : serviceRoutine.getSecurityServiceStrategies()) {
            for (SecurityStrategy strategyService : securityStrategies) {
                if (strategyService.isSupported(authorisationStrategy)) {
                    strategyService.authorise(user.getRoles(), param);
                }
            }
        }
    }
}
