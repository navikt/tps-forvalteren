package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;

@Service
public class CreateMeldingWithMeldingstype {

    @Autowired
    private DetectMeldingstype detectMeldingstype;

    public List<RsMeldingstype> execute(List<String> meldinger) {
        return meldinger.stream()
                .map(detectMeldingstype::execute)
                .collect(Collectors.toList());
    }
}
