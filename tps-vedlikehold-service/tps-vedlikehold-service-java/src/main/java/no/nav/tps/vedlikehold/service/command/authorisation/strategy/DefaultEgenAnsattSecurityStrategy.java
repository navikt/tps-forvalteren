package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisationStrategy;
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
        return a1 instanceof EgenAnsattAuthorisationStrategy;
    }

    @Override
    public void authorise(Set<String> roles, String fodselsnummer) {
        Boolean isEgenAnsatt = egenAnsattConsumer.isEgenAnsatt(fodselsnummer);
        if(isEgenAnsatt && !roles.contains(ROLE_READ_EGENANSATT)){
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
        }
    }
}
