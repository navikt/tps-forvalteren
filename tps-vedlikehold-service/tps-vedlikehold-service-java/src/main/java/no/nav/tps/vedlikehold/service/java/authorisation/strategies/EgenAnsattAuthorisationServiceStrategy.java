package no.nav.tps.vedlikehold.service.java.authorisation.strategies;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.User;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class EgenAnsattAuthorisationServiceStrategy implements AuthorisationServiceStrategy {

    private static final String ROLE_READ_EGENANSATT = "0000-GA-PIP_EGENANSATT";

    private EgenAnsattConsumer egenAnsattConsumer;

    public EgenAnsattAuthorisationServiceStrategy(EgenAnsattConsumer egenAnsattConsumer){
        this.egenAnsattConsumer = egenAnsattConsumer;
    }

    @Override
    public Boolean userIsAuthorisedToReadPerson(User user, String fnr) {
        Boolean isEgenAnsatt = egenAnsattConsumer.isEgenAnsatt(fnr);

        return !isEgenAnsatt || user.getRoles().contains(ROLE_READ_EGENANSATT);
    }
}
