package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.temp;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DefaultDiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.DefaultEgenAnsattConsumer;
import no.nav.tps.vedlikehold.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.vedlikehold.domain.rs.User;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class AuthorisationService {

    private static final String ROLE_READ_DISKRESJONSKODE_6 = "0000-GA-GOSYS_KODE6";
    private static final String ROLE_READ_DISKRESJONSKODE_7 = "0000-GA-GOSYS_KODE7";
    private static final String ROLE_READ_EGENANSATT        = "0000-GA-PIP_EGENANSATT";

    // Mocket for å kunne kjøre lokalt
    private DiskresjonskodeConsumer diskresjonskodeConsumer = new DiskresjonskodeConsumer() {
        @Override
        public boolean ping() throws Exception {
            return true;
        }

        @Override
        public HentDiskresjonskodeResponse getDiskresjonskode(String fNr) throws Exception {
            HentDiskresjonskodeResponse response = new HentDiskresjonskodeResponse();

            response.setDiskresjonskode(fNr.substring(0, 1));

            return response;
        }

        @Override
        public HentDiskresjonskodeBolkResponse getDiskresjonskodeBolk(List<String> fNrListe) {
            return new HentDiskresjonskodeBolkResponse();
        }
    };

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

    public Boolean isAuthorisedToRetrievePerson(String identifier, Set<String> roles) {
        Boolean canRestrieveWithDiskresjonskode = false;
        Boolean isEgenAnsatt                    = egenAnsattConsumer.isEgenAnsatt(identifier);

        try {
            canRestrieveWithDiskresjonskode =  isAuthorisedToRetrievePersonWithDiskresjonskode(identifier ,roles);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return canRestrieveWithDiskresjonskode && !isEgenAnsatt;
    }

    private Boolean isAuthorisedToRetrievePersonWithDiskresjonskode(String identifier, Set<String> roles) throws Exception {
        String diskresjonskode = diskresjonskodeConsumer.getDiskresjonskode(identifier).getDiskresjonskode();

        if (diskresjonskode.equals("6")) {
            return roles.contains(ROLE_READ_DISKRESJONSKODE_6);
        }

        return !diskresjonskode.equals("7") || roles.contains(ROLE_READ_DISKRESJONSKODE_7);
    }
}
