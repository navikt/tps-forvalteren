package no.nav.tps.forvalteren.service.command.authorisation;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsMeldingDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.strategy.SearchSecurityStrategy;
import no.nav.tps.forvalteren.service.command.authorisation.strategy.SecurityStrategy;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.service.command.authorisation.strategy.RestSecurityStrategy;
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
    public void authoriseRestCall(TpsMeldingDefinition serviceRoutine) {
        for (ServiceRutineAuthorisationStrategy authStrategy : serviceRoutine.getRequiredSecurityServiceStrategies()) {
            getUnauthorizedRestStrategies(authStrategy, restSecurityStrategies)
                    .forEach(SecurityStrategy::handleUnauthorised);
        }
    }

    @Override
    public void authorisePersonSearch(TpsMeldingDefinition serviceRoutine, String fnr){
        for (ServiceRutineAuthorisationStrategy authStrategy : serviceRoutine.getRequiredSecurityServiceStrategies()) {
            getUnauthorizedPersonStrategies(authStrategy, searchPersonSecurityStrategies, fnr)
                    .forEach(SecurityStrategy::handleUnauthorised);
        }
    }

    @Override
    public boolean isAuthorisedToUseServiceRutine(TpsMeldingDefinition serviceRoutine){
        return !serviceRoutine.getRequiredSecurityServiceStrategies()
                .stream()
                .anyMatch(strategy -> !isAuthorised(strategy, restSecurityStrategies));
    }

    @Override
    public boolean isAuthorisedToFetchPersonInfo(TpsMeldingDefinition serviceRoutine, String fnr) {
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
