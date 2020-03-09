package no.nav.tps.forvalteren.service.command.testdata.opprett;

import static java.lang.String.*;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.BOST;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.service.command.testdata.FiktiveIdenterGenerator;

@Service
@RequiredArgsConstructor
public class KontonrGeneratorService {

    private static RsPersonKriterier kriterier;

    static {
        kriterier = RsPersonKriterier.builder()
                .antall(1)
                .identtype(BOST.name())
                .build();
    }

    private final FiktiveIdenterGenerator fiktiveIdenterGenerator;

    public String generateNumber(){

        String kontonr = fiktiveIdenterGenerator.genererFiktiveIdenter(kriterier).iterator().next();
        return format("%s.%s.%s", kontonr.substring(0,4), kontonr.substring(4,6), kontonr.substring(6));
    }
}
