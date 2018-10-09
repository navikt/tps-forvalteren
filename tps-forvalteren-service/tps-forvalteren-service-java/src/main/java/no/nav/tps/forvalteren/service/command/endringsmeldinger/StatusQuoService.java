package no.nav.tps.forvalteren.service.command.endringsmeldinger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype;
import no.nav.tps.forvalteren.domain.rs.skd.RsMeldingstype1Felter;

/**
 * Service som gir status quo på en person basert på historikken til personen i en liste med skdmeldinger.
 */
@Service
public class StatusQuoService {
    
    private MapperFacade mapperFacade;
    
    @Autowired
    private SkdEndringsmeldingService skdEndringsmeldingService;
    
    public StatusQuoService() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(false).build();
        mapperFacade = mapperFactory.getMapperFacade();
    }
    
    public List<RsMeldingstype> getStatusQuoOfPeopleInGruppe(Long gruppeId) {
        List<RsMeldingstype> cronologicalHistory = skdEndringsmeldingService.findAllSkdmeldingerByGruppeIdOrderByIdAsc(gruppeId);
        return createStatusQuoList(cronologicalHistory);
    }
    
    public List<RsMeldingstype> createStatusQuoList(List<RsMeldingstype> cronologicalHistory) {
        Map<String, RsMeldingstype1Felter> statusQuoSkdmeldingTrans1 = new HashMap<>();
        for (RsMeldingstype skdmelding : cronologicalHistory) {
            if (skdmelding instanceof RsMeldingstype1Felter) {
                mergeLatestHistoryToMap(((RsMeldingstype1Felter) skdmelding), statusQuoSkdmeldingTrans1);
            }
        }
        return new ArrayList<>(statusQuoSkdmeldingTrans1.values());
    }
    
    public void mergeLatestHistoryToMap(RsMeldingstype1Felter cronologicalHistory, Map<String, RsMeldingstype1Felter> statusQuoSkdmeldingTrans1) {
        statusQuoSkdmeldingTrans1.merge(cronologicalHistory.getFodselsdato() + cronologicalHistory.getPersonnummer(), cronologicalHistory,
                (statusQuo, newMessage) -> {
                    mapperFacade.map(newMessage, statusQuo);
                    return statusQuo;
                });
    }
}