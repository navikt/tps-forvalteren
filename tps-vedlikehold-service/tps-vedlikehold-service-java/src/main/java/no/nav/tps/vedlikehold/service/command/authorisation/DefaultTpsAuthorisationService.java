package no.nav.tps.vedlikehold.service.command.authorisation;

import static no.nav.tps.vedlikehold.service.command.authorisation.RolesService.RoleType.READ;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.Person;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.response.ServiceRoutineResponse;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.AuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.DiskresjonskodeAuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.EgenAnsattAuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.ReadAuthorisationServiceStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class DefaultTpsAuthorisationService implements TpsAuthorisationService {

    @Autowired
    private RolesService rolesService;

    @Autowired
    private DiskresjonskodeConsumer diskresjonskodeConsumer;

    @Autowired
    private EgenAnsattConsumer egenAnsattConsumer;

    /**
     * Convenience method authorising the user based on its roles, 'diskresjonskode', and 'egen ansatt'.
     *
     * @param user user trying to access a person's data
     * @param fnr fnr of the person to be accessed
     * @param environment environment in which to contact TPS
     * @return <code>Boolean</code> indicating whether the user is authorised
     */
    @Override
    public boolean userIsAuthorisedToReadPersonInEnvironment(User user, String fnr, String environment) {

        /* Diskresjonskode */
        DiskresjonskodeAuthorisationServiceStrategy diskresjonskodeStrategy = new DiskresjonskodeAuthorisationServiceStrategy();

        diskresjonskodeStrategy.setDiskresjonskodeConsumer(diskresjonskodeConsumer);
        diskresjonskodeStrategy.setUser(user);
        diskresjonskodeStrategy.setFnr(fnr);

        /* Egen ansatt */
        EgenAnsattAuthorisationServiceStrategy egenAnsattStrategy = new EgenAnsattAuthorisationServiceStrategy();

        egenAnsattStrategy.setEgenAnsattConsumer(egenAnsattConsumer);
        egenAnsattStrategy.setUser(user);
        egenAnsattStrategy.setFnr(fnr);

        /* Read environment */
        ReadAuthorisationServiceStrategy readStrategy = new ReadAuthorisationServiceStrategy();

        readStrategy.setUser(user);
        readStrategy.setReadRoles( rolesService.getRolesForEnvironment(environment, READ) );


        List<AuthorisationServiceStrategy> strategies = Arrays.asList(
                diskresjonskodeStrategy,
                egenAnsattStrategy,
                readStrategy
        );

        return isAuthorised(strategies);
    }

    //TODO Kanskje flytte. Er ikke helt authorizering task.
    @Override
    public ArrayList<Person> getAuthorizedPersons(User user, List<Person> persons, String environment) {
        return persons.stream()
                .filter(person -> userIsAuthorisedToReadPersonInEnvironment(user, person.getFnr(), environment))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean isAuthorizedToReadAtLeastOnePerson(User user, List<Person> persons, String environment){
        if(persons.isEmpty()) return true;
        for(Person person : persons){
            if(userIsAuthorisedToReadPersonInEnvironment(user,person.getFnr(), environment)){
                return true;
            }
        }
        return false;
    }

    /**
     * Authorises the user based on an arbitrary collection of strategies.
     * Should make adding additional authorisation procedures easy as pie.
     *
     * @param strategies authorisation strategies used to authorise the user
     * @return boolean indicating whether the user is authorised
     */
    @Override
    public boolean isAuthorised(Collection<AuthorisationServiceStrategy> strategies) {

        for (AuthorisationServiceStrategy strategy : strategies) {
            if (!strategy.isAuthorised()) {
                return false;
            }
        }

        return true;
    }
}
