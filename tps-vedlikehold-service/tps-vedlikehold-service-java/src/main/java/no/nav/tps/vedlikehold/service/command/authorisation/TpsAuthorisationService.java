package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsMessage;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.person.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface TpsAuthorisationService {
    boolean userIsAuthorisedToReadPersonInEnvironment(TpsMessage msg, TpsRequest request);
    //boolean isAuthorizedToReadAtLeastOnePerson(TpsMessage msg, User user, List<Person> persons, String environment);
    //ArrayList<Person> getAuthorizedPersons(TpsMessage msg, User user, List<Person> persons, String environment);
}
