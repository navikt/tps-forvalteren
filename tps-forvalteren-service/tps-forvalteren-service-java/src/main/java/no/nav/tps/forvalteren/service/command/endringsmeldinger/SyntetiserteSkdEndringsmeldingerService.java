package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;

@Service
public class SyntetiserteSkdEndringsmeldingerService {
    
    public void swapLoepenrMedIdentogLagre(Long avspillergruppeId, List<RsMeldingstype> request, int antallNyeIdenter) {
        //swapLoepenrMedNyeIdent
        //plasserEksisterendeIdenterIMeldinger -
        //lagre til repository
    }
}
