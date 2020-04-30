package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;

@Service
public class HentKjoennFraIdentService {

    public KjoennType execute(String ident){
        int kjoennNummer = Integer.parseInt(ident.substring(8,9));
        return kjoennNummer % 2 == 0 ? KjoennType.K : KjoennType.M;
    }
}
