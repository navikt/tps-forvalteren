package no.nav.tps.forvalteren.service.command.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;

@Service
public class SjekkIdenterForDodsmelding extends SjekkIdenter {

    private static final String IKKE_GYLDIG = "IG";
    private static final String FINNES = "FIN";
    private static final String LEDIG_OG_GYLDIG = "LOG";

    @Autowired
    private SjekkOmGyldigeIdenter sjekkOmGyldigeIdenter;

    @Autowired
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

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
        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(gyldigeIdenter);
        Set<String> environments = new HashSet<>();
        environments.add(miljoe);
        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(gyldigeIdenter, environments);

        Set<String> ledigeIdenterDBOgMiljo = new HashSet<>(ledigeIdenterMiljo);
        ledigeIdenterDBOgMiljo.retainAll(ledigeIdenterDB);

        insertIntoMap(identerMedStatus, ledigeIdenterDBOgMiljo, LEDIG_OG_GYLDIG);
        return ledigeIdenterDBOgMiljo;
    }
}