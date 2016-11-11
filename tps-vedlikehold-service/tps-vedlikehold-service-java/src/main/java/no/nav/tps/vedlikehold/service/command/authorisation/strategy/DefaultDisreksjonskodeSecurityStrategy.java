package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by f148888 on 08.11.2016.
 */

@Component
public class DefaultDisreksjonskodeSecurityStrategy implements DiskresjonskodeSecurityStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDisreksjonskodeSecurityStrategy.class);

    private static final String ROLE_READ_DISKRESJONSKODE_6 = "0000-GA-GOSYS_KODE6";
    private static final String ROLE_READ_DISKRESJONSKODE_7 = "0000-GA-GOSYS_KODE7";

    @Autowired
    private DiskresjonskodeConsumer diskresjonskodeConsumer;

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public boolean isSupported(AuthorisationStrategy a1) {
        return a1 instanceof DiskresjonskodeAuthorisation;
    }

    @Override
    public void authorise(Set<String> userRoles, String fodselsnummer) {
        String diskresjonskode;

        try {
            diskresjonskode = diskresjonskodeConsumer.getDiskresjonskodeResponse(fodselsnummer).getDiskresjonskode();
        } catch (Exception exception) {
            LOGGER.warn("Authorisation denied. Failed to get diskresjonskode with exception: {}", exception.toString());
            throw exception;
        }

        if ("6".equals(diskresjonskode) && !userRoles.contains(ROLE_READ_DISKRESJONSKODE_6)) {
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
        }

        if("7".equals(diskresjonskode) && !userRoles.contains(ROLE_READ_DISKRESJONSKODE_7)){
            throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
        }
    }
}
