package no.nav.tps.forvalteren.service.command.testdata.utils;

import org.springframework.stereotype.Component;

@Component
public class HentIdenttypeFraIdentService {

    public String execute(String ident) {
        if (Integer.parseInt(ident.substring(2, 3)) >= 2) {
            return "BNR";
        } else if (Integer.parseInt(ident.substring(0, 1)) >= 4) {
            return "DNR";
        } else {
            return "FNR";
        }
    }
}