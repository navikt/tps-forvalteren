package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsRawMeldinger;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import no.nav.tps.forvalteren.service.command.testdata.utils.UnmarshalSkdMelding;

@Service
public class CreateAndSaveSkdEndringsmeldingerFromTextService {
    
    @Autowired
    UnmarshalSkdMelding unmarshalSkdMelding;
    @Autowired
    private SplitSkdEndringsmeldingerFromText splitSkdEndringsmeldingerFromText;
    @Autowired
    private SaveSkdEndringsmeldingerService saveSkdEndringsmeldingerService;
    @Autowired
    private CreateMeldingWithMeldingstypeService createMeldingWithMeldingstypeService;
    
    public void execute(Long gruppeId, RsRawMeldinger rawMeldinger) {
        List<String> meldinger = splitSkdEndringsmeldingerFromText.execute(rawMeldinger.getRaw());
        List<SkdMelding> skdMeldinger = meldinger.stream()
                .map(unmarshalSkdMelding::unmarshalMeldingUtenHeader)
                .collect(Collectors.toList());
        List<RsMeldingstype> rsMeldinger = createMeldingWithMeldingstypeService.execute(skdMeldinger);
        saveSkdEndringsmeldingerService.save(rsMeldinger, gruppeId);
    }
    
}
