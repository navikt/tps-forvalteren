package no.nav.tps.forvalteren.service.command.testdata.utils;

import static java.lang.Integer.parseInt;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.BOST;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.DNR;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FDAT;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;

import org.springframework.stereotype.Component;

@Component
public class HentIdenttypeFraIdentService {

    public String execute(String ident) {

        if (parseInt(ident.substring(6, 10)) == 0) {
            return FDAT.name();
        } else if(parseInt(ident.substring(2, 3)) >= 2) {
            return BOST.name();
        } else if (parseInt(ident.substring(0, 1)) >= 4) {
            return DNR.name();
        } else {
            return FNR.name();
        }
    }
}