package no.nav.tps.forvalteren.service.command.testdata.utils;

import static no.nav.tps.forvalteren.common.java.message.MessageConstants.SKD_ILLEGAL_MELDINGSTYPE;
import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.service.command.exceptions.IllegalMeldingstypeException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans2;
import static no.nav.tps.forvalteren.service.command.testdata.skd.impl.SkdFeltDefinisjonerTrans1.TRANSTYPE_END_POSITION;
import static no.nav.tps.forvalteren.service.command.testdata.skd.impl.SkdFeltDefinisjonerTrans1.TRANSTYPE_START_POSITION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnmarshalSkdMelding {

    private MessageProvider messageProvider;

    @Autowired
    public UnmarshalSkdMelding(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    public SkdMelding unmarshalMeldingUtenHeader(String melding) {

        String meldingstype = melding.substring(TRANSTYPE_START_POSITION, TRANSTYPE_END_POSITION);
        switch (meldingstype.charAt(0)) {
        case '1':
            return SkdMeldingTrans1.unmarshal(melding);
        case '2':
        case '3':
        case '4':
            return new SkdMeldingTrans2(melding);
        default:
            throw new IllegalMeldingstypeException(messageProvider.get(SKD_ILLEGAL_MELDINGSTYPE, meldingstype));
        }
    }

}
