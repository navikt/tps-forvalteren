package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by f148888 on 08.11.2016.
 */

@Component
public class DefaultDisreksjonskodeSecurityStrategy implements DiskresjonskodeSecurityStrategy {

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
    public void handleUnauthorised(Set<String> userRoles, String fodselsnummer) {
        throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
    }

    @Override
    public boolean isAuthorised(Set<String> roles, String fnr) {
        String diskresjonskode = diskresjonskodeConsumer.getDiskresjonskodeResponse(fnr).getDiskresjonskode();

        if ("6".equals(diskresjonskode) && !roles.contains(ROLE_READ_DISKRESJONSKODE_6)) {
            return false;
        }

        if("7".equals(diskresjonskode) && !roles.contains(ROLE_READ_DISKRESJONSKODE_7)){
            return false;
        }

        return true;
    }
}
