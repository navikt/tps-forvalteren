package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.consumer.rs.identpool.HentIdenterRequest;
import no.nav.tps.forvalteren.consumer.rs.identpool.IdentPoolClient;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;

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
        List<RsMeldingstype> innvandrOgFoedselsmeldinger = request.stream()
                .filter(mld -> mld instanceof RsMeldingstype1Felter && ("01".equals(mld.getAarsakskode()) || "02".equals(mld.getAarsakskode())))
                .collect(Collectors.toList());
        int antallNyeIdenter = innvandrOgFoedselsmeldinger.size();
        List<String> identer = identPoolClient.hentNyeIdenter(HentIdenterRequest.builder().antall(antallNyeIdenter).foedtEtter(LocalDate.now()).foedtFoer(LocalDate.now()).build());
        for (int i = 0; i < antallNyeIdenter; i++) {
            ((RsMeldingstype1Felter) innvandrOgFoedselsmeldinger.get(i)).setFodselsdato(identer.get(i).substring(0, 6));
            ((RsMeldingstype1Felter) innvandrOgFoedselsmeldinger.get(i)).setPersonnummer(identer.get(i).substring(6));
        }
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
