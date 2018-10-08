package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import static no.nav.tps.forvalteren.consumer.rs.identpool.HentIdenterRequest.IdentType.DNR;
import static no.nav.tps.forvalteren.consumer.rs.identpool.HentIdenterRequest.IdentType.FNR;

import java.util.ArrayList;
import java.util.Arrays;
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
        settInnNyeIdenterIAktuelleMeldinger(FNR, request, Arrays.asList("01", "02", "39"));
        settInnNyeIdenterIAktuelleMeldinger(DNR, request, Arrays.asList("91"));
        //plasserEksisterendeIdenterIMeldinger -
        //lagre til repository
    }
    
    public List<String> settInnNyeIdenterIAktuelleMeldinger(HentIdenterRequest.IdentType identType, List<RsMeldingstype> request, List<String> aarsakskoder) {
        List<RsMeldingstype> aktuelleMeldinger = request.stream()
                .filter(mld -> mld instanceof RsMeldingstype1Felter && aarsakskoder.contains(mld.getAarsakskode()))
                .collect(Collectors.toList());
        int antallNyeIdenter = aktuelleMeldinger.size();
        List<String> identer = identPoolClient.hentNyeIdenter(HentIdenterRequest.builder().antall(antallNyeIdenter).identtype(identType).build());
        for (int i = 0; i < antallNyeIdenter; i++) {
            ((RsMeldingstype1Felter) aktuelleMeldinger.get(i)).setFodselsdato(identer.get(i).substring(0, 6));
            ((RsMeldingstype1Felter) aktuelleMeldinger.get(i)).setPersonnummer(identer.get(i).substring(6));
        }
        return identer;
    }
    
    public void plassereEksisterendeIdenterIMeldinger(List<Long> nyeIdenter, List<RsMeldingstype> request) {
        List<RsMeldingstype> skdmeldingerTrans1 = request.stream().filter(mld -> mld instanceof RsMeldingstype1Felter).collect(Collectors.toList());
        List<RsMeldingstype> skdmeldingerTrans2 = new ArrayList<>(request);
        skdmeldingerTrans2.removeAll(skdmeldingerTrans1);
        
        //bruke enum FelterMedEksisterendeIdenterISkdmeldingTransl1
        //        For hver meldingstype
        //finn alle aktuelle identer som kan brukes som eksisterende ident i gjeldende melding.
        //Trekk tilfeldige identer ut av gruppen
        //putt inn identer
        //hvis det er for få eksisterende identer, så kast feilmelding.
    }
}
