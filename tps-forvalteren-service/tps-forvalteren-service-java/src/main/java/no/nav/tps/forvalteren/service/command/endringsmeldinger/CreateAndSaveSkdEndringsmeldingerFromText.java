package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;

@Service
public class CreateAndSaveSkdEndringsmeldingerFromText {

    @Autowired
    private SplitSkdEndringsmeldingerFromText splitSkdEndringsmeldingerFromText;

    @Autowired
    private SaveSkdEndringsmeldingerFromText saveSkdEndringsmeldingerFromText;

    @Autowired
    private CreateMeldingWithMeldingstype createMeldingWithMeldingstype;

    public void execute(Long gruppeId, RsRawMeldinger rawMeldinger) {
        List<String> meldinger = splitSkdEndringsmeldingerFromText.execute(rawMeldinger.getRaw());
        //TODO Unmarshal meldinger til java-representasjonen SkdMelding
        List<RsMeldingstype> rsMeldinger = createMeldingWithMeldingstype.execute(meldinger);
        saveSkdEndringsmeldingerFromText.execute(rsMeldinger, gruppeId);
    }

}
