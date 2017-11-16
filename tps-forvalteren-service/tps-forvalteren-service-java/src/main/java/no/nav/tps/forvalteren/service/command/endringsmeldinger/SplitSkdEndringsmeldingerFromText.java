package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;

@Service
public class SplitSkdEndringsmeldingerFromText {

    public static final int SKD_ENDRINGSMELDING_LENGTH = 1500;

    public List<String> execute(RsRawMeldinger rawMeldinger) {
        String meldingerAsText = rawMeldinger.getRaw();
        List<String> meldinger = new ArrayList<>();
        if (meldingerAsText.length() % SKD_ENDRINGSMELDING_LENGTH == 0) {
            int startPosition = 0;
            int endPosition = SKD_ENDRINGSMELDING_LENGTH;
            while (startPosition != meldingerAsText.length()) {
                meldinger.add(meldingerAsText.substring(startPosition, endPosition));
                startPosition += SKD_ENDRINGSMELDING_LENGTH;
                endPosition += SKD_ENDRINGSMELDING_LENGTH;
            }
        } else {
            throw new IllegalArgumentException("Teksten har ugyldig lengde: " + meldingerAsText.length());
        }
        return meldinger;
    }

}
