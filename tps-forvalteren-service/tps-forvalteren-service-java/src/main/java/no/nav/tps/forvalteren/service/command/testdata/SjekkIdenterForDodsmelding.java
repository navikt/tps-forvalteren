package no.nav.tps.forvalteren.service.command.testdata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SjekkIdenterForDodsmelding extends SjekkIdenter {

    private static final String IKKE_GYLDIG = "IG";
    private static final String FINNES = "FIN";
    private static final String LEDIG_I_MILJO = "LIM";

    @Autowired
    private DeathRowRepository deathRowRepository;

    @Autowired
    private SjekkOmGyldigeIdenter sjekkOmGyldigeIdenter;


    public Set<IdentMedStatus> finnGyldigeOgLedigeIdenterForDoedsmeldinger(List<String> identer, String miljo) {
        Set<String> ukjenteIdenter = new HashSet<>(identer);
        Map<String, String> identerMedStatus = new HashMap<>();

        Set<String> gyldigeIdenter = sjekkOmGyldigeIdenter.execute(ukjenteIdenter);
        setStatusOnDifference(ukjenteIdenter, gyldigeIdenter, identerMedStatus, IKKE_GYLDIG);

        Set<String> ledigeIdenterDBOgMiljo = finnLedigeIdenterDBOgMiljoOgSetStatus(identerMedStatus, gyldigeIdenter, miljo);
        setStatusOnDifference(gyldigeIdenter, ledigeIdenterDBOgMiljo, identerMedStatus, FINNES);

        return mapToIdentMedStatusSet(identerMedStatus);
    }

    private Set<String> finnLedigeIdenterDBOgMiljoOgSetStatus(Map<String, String> identerMedStatus, Set<String> gyldigeIdenter, String miljoe) {

        Set<String> ledigeIdenterDBOgMiljo = new HashSet<>();


        for (String ident : gyldigeIdenter) {

            if ( deathRowRepository.findByIdentAndMiljoe(ident, miljoe) == null) {
                ledigeIdenterDBOgMiljo.add(ident);
                insertIntoMap(identerMedStatus, ledigeIdenterDBOgMiljo, LEDIG_I_MILJO);
            } else {
                insertIntoMap(identerMedStatus, ledigeIdenterDBOgMiljo, FINNES);
            }
        }

        return ledigeIdenterDBOgMiljo;
    }
}