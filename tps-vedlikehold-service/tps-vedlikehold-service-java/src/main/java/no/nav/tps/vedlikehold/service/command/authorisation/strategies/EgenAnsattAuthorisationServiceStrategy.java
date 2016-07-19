package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.User;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class EgenAnsattAuthorisationServiceStrategy implements AuthorisationServiceStrategy {

    private static final String ROLE_READ_EGENANSATT = "0000-GA-PIP_EGENANSATT";

    private User user;
    private String fnr;
    private EgenAnsattConsumer egenAnsattConsumer;


    @Override
    public Boolean isAuthorised() {
        Boolean isEgenAnsatt = egenAnsattConsumer.isEgenAnsatt(fnr);

        return !isEgenAnsatt || user.getRoles().contains(ROLE_READ_EGENANSATT);
    }

    /* Getters and setters */

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public EgenAnsattConsumer getEgenAnsattConsumer() {
        return egenAnsattConsumer;
    }

    public void setEgenAnsattConsumer(EgenAnsattConsumer egenAnsattConsumer) {
        this.egenAnsattConsumer = egenAnsattConsumer;
    }
}
