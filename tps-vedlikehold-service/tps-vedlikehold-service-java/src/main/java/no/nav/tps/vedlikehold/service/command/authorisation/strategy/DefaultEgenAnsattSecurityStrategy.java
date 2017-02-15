package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by f148888 on 08.11.2016.
 */

@Component
public class DefaultEgenAnsattSecurityStrategy implements EgenAnsattSecurityStrategy {

    private static final String ROLE_READ_EGENANSATT = "0000-GA-GOSYS_UTVIDET";

    @Autowired
    private EgenAnsattConsumer egenAnsattConsumer;

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public boolean isSupported(AuthorisationStrategy a1) {
        return a1 instanceof EgenAnsattAuthorisation;
    }

    @Override
    public void handleUnauthorised(Set<String> roles, String fodselsnummer) {
        throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
    }

    @Override
    public boolean isAuthorised(Set<String> roles, String fnr) {
        return !egenAnsattConsumer.isEgenAnsatt(fnr) || roles.contains(ROLE_READ_EGENANSATT);
    }
}
