package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
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
    public void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine) {
        for (ServiceRutineAuthorisationStrategy authStrategy : serviceRoutine.getRequiredSecurityServiceStrategies()) {
            getUnauthorizedRestStrategies(authStrategy, restSecurityStrategies)
                    .forEach(SecurityStrategy::handleUnauthorised);
        }
    }

    @Override
    public void authorisePersonSearch(TpsServiceRoutineDefinition serviceRoutine, String fnr){
        for (ServiceRutineAuthorisationStrategy authStrategy : serviceRoutine.getRequiredSecurityServiceStrategies()) {
            getUnauthorizedPersonStrategies(authStrategy, searchPersonSecurityStrategies, fnr)
                    .forEach(SecurityStrategy::handleUnauthorised);
        }
    }

    @Override
    public boolean isAuthorisedToUseServiceRutine(TpsServiceRoutineDefinition serviceRoutine){
        return !serviceRoutine.getRequiredSecurityServiceStrategies()
                .stream()
                .anyMatch(strategy -> !isAuthorised(strategy, restSecurityStrategies));
    }

    @Override
    public boolean isAuthorisedToFetchPersonInfo(TpsServiceRoutineDefinition serviceRoutine, String fnr) {
        return !serviceRoutine.getRequiredSecurityServiceStrategies()
                .stream()
                .anyMatch(strategy -> !isAuthorised(strategy, searchPersonSecurityStrategies, fnr));
    }

    private List<SecurityStrategy> getUnauthorizedRestStrategies(ServiceRutineAuthorisationStrategy serviceRutineRequiredAuthorisation, List<RestSecurityStrategy> securityStrategies) {
        return securityStrategies
                .stream()
                .filter(strategy -> strategy.isSupported(serviceRutineRequiredAuthorisation))
                .filter(strategy -> !strategy.isAuthorised())
                .collect(Collectors.toList());
    }

    private List<SecurityStrategy> getUnauthorizedPersonStrategies(ServiceRutineAuthorisationStrategy serviceRutineRequiredAuthorisation, List<SearchSecurityStrategy> securityStrategies, String fnr) {
        return securityStrategies
                .stream()
                .filter(strategy -> strategy.isSupported(serviceRutineRequiredAuthorisation))
                .filter(strategy -> !strategy.isAuthorised(fnr))
                .collect(Collectors.toList());
    }

    private boolean isAuthorised(ServiceRutineAuthorisationStrategy authorisation, List<RestSecurityStrategy> securityStrategies) {
        return !securityStrategies
                .stream()
                .filter(s -> s.isSupported(authorisation))
                .anyMatch(s -> !s.isAuthorised());
    }

    private boolean isAuthorised(ServiceRutineAuthorisationStrategy authorisation, List<SearchSecurityStrategy> securityStrategies,  String fnr) {
        return !securityStrategies
                .stream()
                .filter(s -> s.isSupported(authorisation))
                .anyMatch(s -> !s.isAuthorised(fnr));
    }


}
