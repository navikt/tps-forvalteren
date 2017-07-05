package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SjekkIdenter {

    @Autowired
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Autowired
    private SjekkOmGyldigeIdenter sjekkOmGyldigeIdenter;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    private static final String IKKE_GYLDIG = "IG";
    private static final String IKKE_LEDIG = "IL";
    private static final String LEDIG_OG_GYLDIG = "LOG";

    @PreAuthorize("hasRole('0000-GA-TPSF-LES')")
    public Set<IdentMedStatus> finnGyldigeOgLedigeIdenter(List<String> identListe) {
        Set<String> ukjenteIdenter = new HashSet<>(identListe);
        Map<String, String> identerMedStatus = new HashMap<>();

        Set<String> gyldigeIdenter = sjekkOmGyldigeIdenter.execute(ukjenteIdenter);
        setStatusOnDifference(ukjenteIdenter, gyldigeIdenter, identerMedStatus, IKKE_GYLDIG);

        Set<String> ledigeIdenterDBOgMiljo = finnLedigeIdenterDBOgMiljoOgSetStatus(identerMedStatus, gyldigeIdenter);
        setStatusOnDifference(gyldigeIdenter, ledigeIdenterDBOgMiljo, identerMedStatus, IKKE_LEDIG);
        return mapToIdentMedStatusSet(identerMedStatus);
    }

    private void setStatusOnDifference(Set<String> firstIdentSet, Set<String> secondIdentSet, Map<String, String> identerMedStatus, String status) {
        Set<String> identer = new HashSet<>(firstIdentSet);
        identer.removeAll(secondIdentSet);
        insertIntoMap(identerMedStatus, identer, status);
    }

    private Set<String> finnLedigeIdenterDBOgMiljoOgSetStatus(Map<String, String> identerMedStatus, Set<String> gyldigeIdenter) {
        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(gyldigeIdenter);
        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligeIMiljo.filtrer(gyldigeIdenter);

        Set<String> ledigeIdenterDBOgMiljo = new HashSet<>();
        ledigeIdenterDBOgMiljo.addAll(ledigeIdenterMiljo);
        ledigeIdenterDBOgMiljo.retainAll(ledigeIdenterDB);

        insertIntoMap(identerMedStatus, ledigeIdenterDBOgMiljo, LEDIG_OG_GYLDIG);
        return ledigeIdenterDBOgMiljo;
    }

    private Set<IdentMedStatus> mapToIdentMedStatusSet(Map<String, String> identer) {
        Set<IdentMedStatus> identerMedStatus = new HashSet<>();
        for (Map.Entry<String, String> entry : identer.entrySet()) {
            identerMedStatus.add(new IdentMedStatus(entry.getKey(), entry.getValue()));
        }
        return identerMedStatus;
    }

    private void insertIntoMap(Map<String, String> identer, Set<String> gyldigeIdenter, String status) {
        for (String ident : gyldigeIdenter) {
            identer.put(ident, status);
        }
    }

}
