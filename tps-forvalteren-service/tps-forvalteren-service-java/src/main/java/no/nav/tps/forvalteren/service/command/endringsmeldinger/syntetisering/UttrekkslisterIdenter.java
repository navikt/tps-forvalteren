package no.nav.tps.forvalteren.service.command.endringsmeldinger.syntetisering;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

public class UttrekkslisterIdenter {
    
    @Autowired
    private SkdEndringsmeldingRepository repository;
    
    private List<String> levende;
    private List<SkdEndringsmelding> doed;
    private List<String> singel;
    private List<SkdEndringsmelding> vigslede;
    
    public List<String> getLevende() {
        if (levende == null) {
        
        }
        return levende;
    }
    
    public List<SkdEndringsmelding> getDoed() {
        if (doed == null) {
            //            doed = repository.findByAarsakskodeInAndOrderByIdDesc(Arrays.asList("43"));
            //trekk fra annulerte?
        }
        return doed;
    }
    
    public List<String> getSingel() {
        if (singel == null) {
            //            List<SkdEndringsmelding> alleOpprettede = repository.findByAarsakskodeInAndOrderByIdDesc(Arrays.asList("01", "02", "91"));
            //            List<SkdEndringsmelding> alleKorrigerteFNR = repository.findByAarsakskodeInAndOrderByIdDesc(Arrays.asList("39"));
            //            List<SkdEndringsmelding> alleUtgåtte = repository.findByAarsakskodeInAndOrderByIdDesc(Arrays.asList("32"));
            //kanskje kun få foedselsnummer fra DB, og bruke alleOpprettede.removeAll(). Gir bedre ytelse
            //singel=alleOpprettede.removeAll(vigslede)
        }
        return singel;
    }
    
    public List<SkdEndringsmelding> getVigslede() {
        if (vigslede == null) {
            //            vigslede = repository.findByAarsakskodeInAndOrderByIdDesc(Arrays.asList("11"));
        }
        return vigslede;
    }
}
