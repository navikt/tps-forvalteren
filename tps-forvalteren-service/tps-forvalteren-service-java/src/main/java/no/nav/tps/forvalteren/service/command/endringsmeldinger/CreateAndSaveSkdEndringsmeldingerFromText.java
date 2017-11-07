package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAndSaveSkdEndringsmeldingerFromText {

    @Autowired
    private CreateSkdEndringsmeldingFromText createSkdEndringsmeldingFromText;

    @Autowired
    private SaveSkdEndringsmeldingerFromText saveSkdEndringsmeldingerFromText;

    public void execute(String meldingerAsText, Long gruppeId) {
        List<String> meldinger = createSkdEndringsmeldingFromText.execute(meldingerAsText);
        saveSkdEndringsmeldingerFromText.execute(meldinger, gruppeId);
    }
}
