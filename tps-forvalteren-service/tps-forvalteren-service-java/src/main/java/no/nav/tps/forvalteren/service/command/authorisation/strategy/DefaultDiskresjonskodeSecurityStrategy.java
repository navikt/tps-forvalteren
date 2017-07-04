package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DefaultDiskresjonskodeSecurityStrategy implements DiskresjonskodeSecurityStrategy {

    private static final String KODE_SEKS = "6";
    private static final String KODE_SYV = "7";

    @Autowired
    private DiskresjonskodeConsumer diskresjonskodeConsumer;

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private UserContextHolder userContextHolder;

    @Override
    public boolean isSupported(ServiceRutineAuthorisationStrategy a1) {
        return a1 instanceof DiskresjonskodeServiceRutineAuthorisation;
    }

    @Override
    public void handleUnauthorised() {
        throw new HttpForbiddenException(messageProvider.get("rest.service.request.exception.Forbidden"), "api/v1/service/");
    }

    @Override
    public boolean isAuthorised(String fnr) {
        String diskresjonskodeRequired = diskresjonskodeConsumer.getDiskresjonskodeResponse(fnr).getDiskresjonskode();

        if (KODE_SEKS.equals(diskresjonskodeRequired) && !userContextHolder.getRoles().contains(UserRole.ROLE_DISKRESJONESKODE_6_READ)) {
            return false;
        }

        if(KODE_SYV.equals(diskresjonskodeRequired) && !userContextHolder.getRoles().contains(UserRole.ROLE_DISKRESJONESKODE_7_READ)){
            return false;
        }

        return true;
    }
}
