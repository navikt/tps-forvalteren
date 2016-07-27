package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.AuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.DiskresjonskodeAuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.EgenAnsattAuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.ReadAuthorisationServiceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static no.nav.tps.vedlikehold.service.command.authorisation.RolesManager.RoleType.READ;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class DefaultAuthorisationService implements AuthorisationService {

    @Autowired
    private RolesManager rolesManager;

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
    public Boolean userIsAuthorisedToReadPersonInEnvironment(User user, String fnr, String environment) {

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
        readStrategy.setReadRoles( rolesManager.getRolesForEnvironment(environment, READ) );


        List<AuthorisationServiceStrategy> strategies = Arrays.asList(
                diskresjonskodeStrategy,
                egenAnsattStrategy,
                readStrategy
        );

        return isAuthorised(strategies);
    }

    /**
     * Authorises the user based on an arbitrary collection of strategies.
     * Should make adding additional authorisation procedures easy as pie.
     *
     * @param strategies authorisation strategies used to authorise the user
     * @return boolean indicating whether the user is authorised
     */
    @Override
    public Boolean isAuthorised(Collection<AuthorisationServiceStrategy> strategies) {

        for (AuthorisationServiceStrategy strategy : strategies) {
            if (!strategy.isAuthorised()) {
                return false;
            }
        }

        return true;
    }
}
