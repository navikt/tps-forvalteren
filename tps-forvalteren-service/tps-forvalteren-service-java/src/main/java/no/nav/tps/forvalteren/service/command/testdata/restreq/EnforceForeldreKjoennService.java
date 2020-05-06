package no.nav.tps.forvalteren.service.command.testdata.restreq;

import java.security.SecureRandom;
import java.util.Random;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;

@Service
public class EnforceForeldreKjoennService {

    private Random random = new SecureRandom();

    public void setDefaultKjoenn(RsPersonBestillingKriteriumRequest request) {

        if (!request.hasSpesificKjoenn()) {
            if (request.getRelasjoner().getPartnere().isEmpty() ||
                    !request.getRelasjoner().getPartnere().get(0).hasSpesificKjoenn()) {
                request.setKjonn(random.nextBoolean() ? KjoennType.K : KjoennType.M);

            } else if (!request.getRelasjoner().getPartnere().isEmpty()) {
                request.setKjonn(request.getRelasjoner().getPartnere().get(0).isMann() ?
                        getWeightedKjonn(KjoennType.K) : getWeightedKjonn(KjoennType.M));
            }
        }
        request.getRelasjoner().getPartnere().forEach(partner -> {
            if (!partner.hasSpesificKjoenn()) {
                partner.setKjonn(request.isMann() ? getWeightedKjonn(KjoennType.K) : getWeightedKjonn(KjoennType.M));
            }
        });
    }

    private KjoennType getWeightedKjonn(KjoennType kjoenn) {

        if (random.nextFloat() <= .95) {
            return kjoenn;
        } else {
            return KjoennType.getMotsattKjoenn(kjoenn);
        }
    }
}