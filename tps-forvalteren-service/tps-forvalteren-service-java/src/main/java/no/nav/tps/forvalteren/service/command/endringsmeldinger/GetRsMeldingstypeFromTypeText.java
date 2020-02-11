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
public class GetRsMeldingstypeFromTypeText {

    @Autowired
    private MessageProvider messageProvider;

    public RsMeldingstype execute(String meldingstype) {
        if ("t1".equalsIgnoreCase(meldingstype)) {
            return new RsMeldingstype1Felter();
        } else if ("t2".equalsIgnoreCase(meldingstype)) {
            return new RsMeldingstype2Felter();
        } else {
            throw new IllegalMeldingstypeException(messageProvider.get(SKD_ILLEGAL_MELDINGSTYPE, meldingstype));
        }
    }
}
