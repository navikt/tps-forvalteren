package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.User;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.AuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.DiskresjonskodeAuthorisationServiceStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.strategies.EgenAnsattAuthorisationServiceStrategy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class AuthorisationService {

//    @Autowired
    /* FIXME: Remove these mock when TPSWS is up and running */
    private DiskresjonskodeConsumer diskresjonskodeConsumer = new DiskresjonskodeConsumer() {
        @Override
        public boolean ping() throws Exception {
            return true;
        }

        @Override
        public HentDiskresjonskodeResponse getDiskresjonskode(String fNr) throws Exception {
            HentDiskresjonskodeResponse response = new HentDiskresjonskodeResponse();
            response.setDiskresjonskode("1");
            return response;
        }

        @Override
        public HentDiskresjonskodeBolkResponse getDiskresjonskodeBolk(List<String> fNrListe) {
            return null;
        }
    };

//    @Autowired
    private EgenAnsattConsumer egenAnsattConsumer = new EgenAnsattConsumer() {
        @Override
        public boolean ping() throws Exception {
            return true;
        }

        @Override
        public boolean isEgenAnsatt(String fnr) {
            return false;
        }
    };

    /**
     * Convenience method authorising the user based on 'diskresjonskode' and 'egen ansatt'.
     *
     * @param user user trying to access a person's data
     * @param fnr fnr of the person to be accessed
     * @return boolean indicating whether the user is authorised
     */
    public Boolean userIsAuthorisedToReadPerson(User user, String fnr) {

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

        List<AuthorisationServiceStrategy> strategies = Arrays.asList(diskresjonskodeStrategy, egenAnsattStrategy);

        return isAuthorised(strategies);
    }

    /**
     * Authorises the user based on an arbitrary collection of strategies.
     * Should make adding additional authorisation procedures easy as pie.
     *
     * @param strategies authorisation strategies used to authorise the user
     * @return boolean indicating whether the user is authorised
     */
    public Boolean isAuthorised(Collection<AuthorisationServiceStrategy> strategies) {

        for (AuthorisationServiceStrategy strategy : strategies) {
            if (!strategy.isAuthorised()) {
                return false;
            }
        }

        return true;
    }
}
