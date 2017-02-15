package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.User.User;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.RestSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SearchSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SecurityStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultTpsAuthorisationService implements TpsAuthorisationService {

    @Autowired
    private List<SearchSecurityStrategy> searchPersonSecurityStrategies;

    @Autowired
    private List<RestSecurityStrategy> restSecurityStrategies;

    @Override
    public void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine, String environment, User user) {
        for (AuthorisationStrategy auth: serviceRoutine.getSecurityServiceStrategies()) {
            getUnauthorizedStrategies(auth, user, environment, restSecurityStrategies)
                    .stream()
                    .forEach(s -> s.handleUnauthorised(user.getRoles(), environment));
        }
    }

    public boolean isAuthorisedToSeePerson(TpsServiceRoutineDefinition serviceRoutine, String fnr, User user) {
        return !serviceRoutine.getSecurityServiceStrategies()
                .stream()
                .filter(s -> !isAuthorised(s, user, fnr, searchPersonSecurityStrategies))
                .findFirst()
                .isPresent();
    }


    private List<SecurityStrategy> getUnauthorizedStrategies (AuthorisationStrategy authorisation, User user, String param, List<? extends SecurityStrategy> securityStrategies) {
        return securityStrategies
                .stream()
                .filter(s -> s.isSupported(authorisation))
                .filter(s -> !s.isAuthorised(user.getRoles(), param))
                .collect(Collectors.toList());
    }

    private boolean isAuthorised (AuthorisationStrategy authorisation, User user, String param, List<? extends SecurityStrategy> securityStrategies) {
        return !securityStrategies
                .stream()
                .filter(s -> s.isSupported(authorisation))
                .filter(s -> !s.isAuthorised(user.getRoles(), param))
                .findFirst()
                .isPresent();
    }


}
