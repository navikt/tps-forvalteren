package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ILLEGAL_MELDINGSTYPE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype2Felter;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalMeldingstypeException;

@Service
public class DetectMeldingstype {

    @Autowired
    private MessageProvider messageProvider;

    public RsMeldingstype execute(String melding) {
        String meldingstype = melding.substring(25, 26);
        switch (meldingstype) {
        case "1":
            return new RsMeldingstype1Felter();
        case "2":
            return new RsMeldingstype2Felter();
        case "3":
            return new RsMeldingstype2Felter();
        case "4":
            return new RsMeldingstype2Felter();
        default:
            throw new IllegalMeldingstypeException(messageProvider.get(SKD_ILLEGAL_MELDINGSTYPE, meldingstype));
        }
    }

}