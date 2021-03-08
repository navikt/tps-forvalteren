package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;


@Component
public class DefaultEgenAnsattSecurityStrategy implements EgenAnsattSecurityStrategy {

    @Autowired
    private EgenAnsattConsumer egenAnsattConsumer;

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private UserContextHolder userContextHolder;

    @Override
    public boolean isSupported(ServiceRutineAuthorisationStrategy a1) {
        return a1 instanceof EgenAnsattServiceRutineAuthorisation;
    }

    @Override
    public void handleForbiddenCall() {
        throw new HttpForbiddenException(messageProvider.get("rest.service.request.exception.Forbidden"));
    }

    @Override
    public boolean isAuthorised(String fnr) {
        return !egenAnsattConsumer.isEgenAnsatt(fnr) || userContextHolder.getRoles().contains(UserRole.ROLE_EGEN_ANSATT_READ);
    }
}
