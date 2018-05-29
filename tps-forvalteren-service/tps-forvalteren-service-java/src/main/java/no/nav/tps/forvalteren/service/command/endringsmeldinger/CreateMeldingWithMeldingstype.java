package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMelding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;

@Service
public class CreateMeldingWithMeldingstype {

    @Autowired
    private MapToRsMelding mapToRsMelding;

    public List<RsMeldingstype> execute(List<SkdMelding> meldinger) {
        return meldinger.stream()
                .map(mapToRsMelding::execute)
                .collect(Collectors.toList());
    }
}
