package no.nav.tps.forvalteren.service.command.endringsmeldinger.syntetisering;

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
        List<RsMeldingstype> nyeSkdmeldingerTrans1 = request.stream().filter(mld -> mld instanceof RsMeldingstype1Felter).collect(Collectors.toList());
        List<RsMeldingstype> nyeSkdmeldingerTrans2 = new ArrayList<>(request);
        nyeSkdmeldingerTrans2.removeAll(nyeSkdmeldingerTrans1);
        
        //sorter nyeMeldinger etter årsakskode. Plasser liste i Map.
        //Iterere gjennom årsakskodene. dette minimerer arbeidet. switch case? eller generisk impl. for hver årsakskode?
        UttrekkslisterIdenter uttrekkslisterIdenter;
        int antall = 1;
        String aarsakskode = "";
        List<String> eksisterendeIdenter = finnEksisterendeIdentBasertPaaKategori(aarsakskode, antall);
        
        //if-statement for hver status quo info som trengs for meldingen om identen: getLastMeldingWithOneOfAarsakskoderForIdent(ident,List<String> aarsakskoder)
        
        //List<String> aktiveIdenter - alle opprettelser minus utgåtte minus døde fra repository
        //singleIdenter -
        //gifteIdenter - hentes direkte fra DB. //Trekke fra skilte personer?
        //bruke enum FelterMedEksisterendeIdenterISkdmeldingTransl1
        //        For hver meldingstype
        //finn alle aktuelle identer som kan brukes som eksisterende ident i gjeldende melding.
        //Trekk tilfeldige identer ut av gruppen
        //putt inn identer
        //hvis det er for få eksisterende identer, så kast feilmelding.
    }
    
    private List<String> finnEksisterendeIdentBasertPaaKategori(final String aarsakskode, int antall) {
        //start med switch case årsakskode, for å hente FNR basert på kriterier
        switch (aarsakskode) {
        case "39":
        case "07":
        case "10":
        case "24":
        case "26":
        case "56":
        case "35":
        case "44":
        case "47":
        case "51":
        case "81":
        case "98":
        case "38":
        case "28":
        case "29":
        case "48":
        case "49":
        case "43":
        case "32":
            //levende
            break;
        
        case "34":
            //GIFT eller levende (statistikk?
            break;
        case "40":
            //            barn/fødselsmelding
            break;
        case "41": //Endring av oppholdstillatelse
            //innvandret
            break;
        case "25":
            //Annulere flytting
            break;
        case "85":    //sivilstandsendring . Melding om enke/-mann/gjenlevende partner
            //        NY_DØDSMELDING eller GIFT
            break;
        case "11":
            //Singel
            break;
        case "14":
        case "18":
            //GIFT
            break;
        case "06"://Navneendring - første gangs navnevalg"
            //LEVENDE eller FØRSTEGANGS_NAVNEENDRING
            break;
        }
        return null;
    }
}
