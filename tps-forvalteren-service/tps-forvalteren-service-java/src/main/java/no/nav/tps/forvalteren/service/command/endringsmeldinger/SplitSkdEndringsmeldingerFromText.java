package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.common.message.MessageConstants.SKD_ENDRINGSMELDING_ILLEGAL_LENGTH;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.service.command.exceptions.SkdEndringsmeldingIllegalLengthException;

@Service
public class SplitSkdEndringsmeldingerFromText {

    @Autowired
    private MessageProvider messageProvider;

    private static final int SKD_ENDRINGSMELDING_LENGTH = 1500;

    public List<String> execute(String meldingerAsText) {
        if (meldingerAsText.length() % SKD_ENDRINGSMELDING_LENGTH == 0) {
            List<String> meldinger = Lists.newArrayListWithExpectedSize(meldingerAsText.length());
            int startPosition = 0;
            while (startPosition != meldingerAsText.length()) {
                meldinger.add(meldingerAsText.substring(startPosition, startPosition + SKD_ENDRINGSMELDING_LENGTH));
                startPosition += SKD_ENDRINGSMELDING_LENGTH;
            }
            return meldinger;
        } else {
            throw new SkdEndringsmeldingIllegalLengthException(messageProvider.get(SKD_ENDRINGSMELDING_ILLEGAL_LENGTH, meldingerAsText.length()));
        }
    }
}