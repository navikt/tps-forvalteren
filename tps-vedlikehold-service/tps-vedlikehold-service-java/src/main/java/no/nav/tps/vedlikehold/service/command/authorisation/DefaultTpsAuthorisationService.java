package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.RestSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SearchSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SecurityStrategy;
import no.nav.tps.vedlikehold.service.user.UserContextHolder;
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

    @Autowired
    private UserContextHolder userContextHolder;

    @Override
    public void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine) {
        for (ServiceRutineAuthorisationStrategy authStrategy : serviceRoutine.getRequiredSecurityServiceStrategies()) {
            getUnauthorizedStrategies(authStrategy, restSecurityStrategies)
                    .forEach(SecurityStrategy::handleUnauthorised);
        }
    }

    @Override
    public boolean isAuthorisedToSeePerson(TpsServiceRoutineDefinition serviceRoutine, String fnr) {
        return !serviceRoutine.getRequiredSecurityServiceStrategies()
                .stream()
                .anyMatch(s -> !isAuthorised(s,  fnr, searchPersonSecurityStrategies));
    }

    @Override
    public boolean isAuthorisedToUseServiceRutine(TpsServiceRoutineDefinition serviceRoutine){
        return !serviceRoutine.getRequiredSecurityServiceStrategies()
                .stream()
                .anyMatch(strategy -> !isAuthorised(strategy, restSecurityStrategies));
    }


    private List<SecurityStrategy> getUnauthorizedStrategies(ServiceRutineAuthorisationStrategy srRequiredAuthorisation, List<RestSecurityStrategy> securityStrategies) {
        return securityStrategies
                .stream()
                .filter(strategy -> strategy.isSupported(srRequiredAuthorisation))
                .filter(strategy -> !strategy.isAuthorised(userContextHolder.getRoles()))
                .collect(Collectors.toList());
    }


    private boolean isAuthorised(ServiceRutineAuthorisationStrategy authorisation, List<RestSecurityStrategy> securityStrategies) {
        return !securityStrategies
                .stream()
                .filter(s -> s.isSupported(authorisation))
                .anyMatch(s -> !s.isAuthorised(userContextHolder.getRoles()));
    }

    private boolean isAuthorised(ServiceRutineAuthorisationStrategy authorisation,  String fnr, List<SearchSecurityStrategy> securityStrategies) {
        return !securityStrategies
                .stream()
                .filter(s -> s.isSupported(authorisation))
                .anyMatch(s -> !s.isAuthorised(userContextHolder.getRoles(), fnr));
    }


}
