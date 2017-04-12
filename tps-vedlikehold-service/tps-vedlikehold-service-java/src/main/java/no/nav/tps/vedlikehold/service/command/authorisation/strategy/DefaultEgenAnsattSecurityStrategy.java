package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.service.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class DefaultEgenAnsattSecurityStrategy implements EgenAnsattSecurityStrategy {

    @Autowired
    private EgenAnsattConsumer egenAnsattConsumer;

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public boolean isSupported(ServiceRutineAuthorisationStrategy a1) {
        return a1 instanceof EgenAnsattServiceRutineAuthorisation;
    }

    @Override
    public void handleUnauthorised() {
        throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
    }

    @Override
    public boolean isAuthorised(Set<UserRole> roles, String fnr) {
        return !egenAnsattConsumer.isEgenAnsatt(fnr) || roles.contains(UserRole.ROLE_EGEN_ANSATT_READ);
    }
}
