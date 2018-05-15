package no.nav.tps.forvalteren.service.command.testdata;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Vergemaal;
import no.nav.tps.forvalteren.domain.rs.RsVergemaal;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpprettVergemaal {

    @Autowired
    private VergemaalRepository vergemaalRepository;

    @Autowired
    private MapperFacade mapper;

    public void execute(RsVergemaal rsVergemaal) {
        Vergemaal vergemaal = mapper.map(rsVergemaal, Vergemaal.class);
        Vergemaal vergemaalIDB = vergemaalRepository.findBySaksidAndInternVergeId(vergemaal.getSaksid(), vergemaal.getInternVergeId());

        if(vergemaalIDB != null){
            if(!sjekkOmVergemaalSkalSlettes(vergemaal)){
                vergemaal.setId(vergemaalIDB.getId());
            }
        }
        vergemaalRepository.save(vergemaal);
    }

    private boolean sjekkOmVergemaalSkalSlettes(Vergemaal vergemaal) {
        if (vergemaal.getEmbete() != null)
            return false;
        if (vergemaal.getSakstype() != null)
            return false;
        if (vergemaal.getMandattype() != null)
            return false;
        if (vergemaal.getVergetype() != null)
            return false;
        return true;

    }
}
