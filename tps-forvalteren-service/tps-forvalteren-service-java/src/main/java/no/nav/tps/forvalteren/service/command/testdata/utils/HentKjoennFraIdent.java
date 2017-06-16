package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.springframework.stereotype.Service;

@Service
public class HentKjoennFraIdent {

    public char execute(String ident){
        int kjoennNummer = Integer.parseInt(ident.substring(8,9));
        return kjoennNummer % 2 == 0 ? 'K' : 'M';
    }
}
