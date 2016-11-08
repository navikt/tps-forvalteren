package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.person.Person;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.*;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsMessage;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SecurityStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class DefaultTpsAuthorisationService implements TpsAuthorisationService {

    @Autowired
    private SecurityStrategyService strategiesService;

     @Autowired
     private UserContextHolder userContextHolder;

    @Override
    public boolean userIsAuthorisedToReadPersonInEnvironment(TpsMessage tpsMessage, TpsRequest request) {
        for (SecurityStrategy strategyService : strategiesService.getSecurityStrategies()) {
            for (AuthorisationStrategy authorisationStrategy : tpsMessage.getSecuritySearchAuthorisationStrategies()) {
                if (strategyService.isSupported(authorisationStrategy)) {
                    String param = request.getParamValue(authorisationStrategy.getRequiredParamKeyName());
                    if (!strategyService.isAuthorised(userContextHolder.getUser().getRoles(), param)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //TODO Kanskje flytte. Er ikke helt authorizering task.
    // Tanken var å uncommente senere når jeg skulle legge til Authorsering for svar med flere resultater
    /*
    @Override
    public ArrayList<Person> getAuthorizedPersons(TpsMessage tpsMessage, User user, List<Person> persons, String environment) {
        return persons.stream()
                .filter(person -> userIsAuthorisedToReadPersonInEnvironment(tpsMessage, user, person.getFnr(), environment))
                .collect(Collectors.toCollection(ArrayList::new));
    }*/

    /*@Override
    public boolean isAuthorizedToReadAtLeastOnePerson(TpsMessage tpsMessage, User user, List<Person> persons, String environment){
        if(persons.isEmpty()) return true;
        for(Person person : persons){
            if(userIsAuthorisedToReadPersonInEnvironment(tpsMessage, )){
                return true;
            }
        }
        return false;
    }*/
}
