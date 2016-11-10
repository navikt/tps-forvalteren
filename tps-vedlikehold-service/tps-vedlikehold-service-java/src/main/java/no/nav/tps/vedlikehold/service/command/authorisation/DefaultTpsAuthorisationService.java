package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.Response;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.RestSecurityStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategy.SearchSecurityStrategy;
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
    private List<SearchSecurityStrategy> searchSecurityStrategies;

    @Autowired
    private List<RestSecurityStrategy> restSecurityStrategies;

    @Override
    public void authoriseRequest(TpsServiceRoutine serviceRoutine, TpsRequest request, User user) {
        authorise(searchSecurityStrategies, serviceRoutine, request, user);
    }

    public void authoriseRest(TpsServiceRoutine serviceRoutine, TpsRequest request, User user){
        authorise(restSecurityStrategies, serviceRoutine, request, user);
    }

    public void authoriseResponse(Response response, TpsServiceRoutine serviceRoutine, TpsRequest request, User user){

    }

    private void authorise(List<? extends SecurityStrategy> securityStrategies, TpsServiceRoutine serviceRoutine, TpsRequest request, User user){
        for (AuthorisationStrategy authorisationStrategy : serviceRoutine.getSecurityServiceStrategies()) {
            for (SecurityStrategy strategyService : securityStrategies) {
                if (strategyService.isSupported(authorisationStrategy)) {
                    String param = request.getParamValue(authorisationStrategy.getRequiredParamKeyName());
                    strategyService.authorise(user.getRoles(), param);
                }
            }
        }

    }
}

//TODO Kanskje flytte. Er ikke helt authorizering task.
// Tanken var å uncommente senere når jeg skulle legge til Authorsering for svar med flere resultater
    /*
    @Override
    public ArrayList<Person> getAuthorizedPersons(TpsMessage tpsMessage, User user, List<Person> persons, String environment) {
        return persons.stream()
                .filter(person -> authoriseRequest(tpsMessage, user, person.getFnr(), environment))
                .collect(Collectors.toCollection(ArrayList::new));
    }*/

    /*@Override
    public boolean isAuthorizedToReadAtLeastOnePerson(TpsMessage tpsMessage, User user, List<Person> persons, String environment){
        if(persons.isEmpty()) return true;
        for(Person person : persons){
            if(authoriseRequest(tpsMessage, )){
                return true;
            }
        }
        return false;
    }*/

