package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;

@Service
public class CreateSkdEndringsmeldingFromText {

    public List<String> execute(RsRawMeldinger rawMeldinger) {
        String meldingerAsText = rawMeldinger.getRaw();
        List<String> meldinger = new ArrayList<>();
        if (meldingerAsText.length() % 1500 == 0) {
            int startPosition = 0;
            int endPosition = 1500;
            while (startPosition != meldingerAsText.length()) {
                meldinger.add(meldingerAsText.substring(startPosition, endPosition));
                startPosition += 1500;
                endPosition += 1500;
            }
        } else {
            throw new IllegalArgumentException("Teksten har ugyldig lengde: " + meldingerAsText.length());
        }
        return meldinger;
    }

}
