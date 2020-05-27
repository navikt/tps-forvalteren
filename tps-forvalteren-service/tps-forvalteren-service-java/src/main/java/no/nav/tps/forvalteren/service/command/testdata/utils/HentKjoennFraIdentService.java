package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.lang.Integer.parseInt;

import java.security.SecureRandom;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;

@Service
public class HentKjoennFraIdentService {

    private static SecureRandom secureRandom = new SecureRandom();

    public KjoennType execute(String ident){

        if (parseInt(ident.substring(6,10)) == 0) {
            return secureRandom.nextBoolean() ? KjoennType.M : KjoennType.K;
        }

        int kjoennNummer = parseInt(ident.substring(8,9));
        return kjoennNummer % 2 == 0 ? KjoennType.K : KjoennType.M;
    }
}