package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.Person;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface TpsAuthorisationService extends AuthorisationService {
    boolean userIsAuthorisedToReadPersonInEnvironment(User user, String fnr, String environment);
    boolean isAuthorizedToReadAtLeastOnePerson(User user, List<Person> persons, String environment);
    ArrayList<Person> getAuthorizedPersons(User user, List<Person> persons, String environment);

}
