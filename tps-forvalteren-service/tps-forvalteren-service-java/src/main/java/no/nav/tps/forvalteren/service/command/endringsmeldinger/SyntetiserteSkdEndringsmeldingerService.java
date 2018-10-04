package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.rs.identpool.HentIdenterRequest;
import no.nav.tps.forvalteren.consumer.rs.identpool.IdentPoolClient;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;

@Service
public class SyntetiserteSkdEndringsmeldingerService {
    
    @Autowired
    private IdentPoolClient identPoolClient;
    
    public void swapLoepenrMedIdentogLagre(Long avspillergruppeId, List<RsMeldingstype> request) {
        swapLoepenrMedNyeIdenter(request);
        //plasserEksisterendeIdenterIMeldinger -
        //lagre til repository
    }
    
    public List<String> swapLoepenrMedNyeIdenter(List<RsMeldingstype> request) {
        //int antallNyeIdenter = tell antall fødselsmelding og innvandringsmeldinger
        List<String> identer = identPoolClient.hentNyeIdenter(HentIdenterRequest.builder().antall(1).foedtEtter(LocalDate.now()).foedtFoer(LocalDate.now()).build());
        //bytt ut loepenummer i fødselsmelding og innvandringsmelding med nye fødselsnumre
        //
        return identer;
    }
    
    public void plasserEksisterendeIdenterIMeldinger(List<Long> nyeIdenter, List<RsMeldingstype> request) {
        //        For hver meldingstype
        //finn alle aktuelle identer som kan brukes som eksisterende ident i gjeldende melding.
        //Trekk tilfeldige identer ut av gruppen
        //putt inn identer
        //hvis det er for få eksisterende identer, så kast feilmelding.
    }
}
