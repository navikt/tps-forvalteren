package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;

@Service
public class CreateAndSaveSkdEndringsmeldingerFromText {

    @Autowired
    private CreateSkdEndringsmeldingFromText createSkdEndringsmeldingFromText;

    @Autowired
    private SaveSkdEndringsmeldingerFromText saveSkdEndringsmeldingerFromText;

    @Autowired
    private CreateMeldingWithMeldingstype createMeldingWithMeldingstype;

    public void execute(RsRawMeldinger rawMeldinger, Long gruppeId) {
        List<String> meldinger = createSkdEndringsmeldingFromText.execute(rawMeldinger);
        createMeldingWithMeldingstype.execute(meldinger);
        saveSkdEndringsmeldingerFromText.execute(meldinger, gruppeId);
    }
}
