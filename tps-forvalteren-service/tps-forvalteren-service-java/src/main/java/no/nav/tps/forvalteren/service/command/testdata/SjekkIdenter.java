package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import org.springframework.beans.factory.annotation.Autowired;
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
    private sjekkOmGyldigeIdenter sjekkOmGyldigeIdenter;

    @Autowired
    private FilterPaaIdenterTilgjengeligeIMiljo filterPaaIdenterTilgjengeligeIMiljo;

    private static final String GYLDIG = "G";
    private static final String IKKE_GYLDIG = "IG";
    private static final String IKKE_LEDIG = "IL";
    private static final String LEDIG_OG_GYLDIG = "LOG";


    public Set<IdentMedStatus> finnGyldigeOgLedigeIdenter(List<String> identListe) {
        Set<IdentMedStatus> identerMedStatus = new HashSet<>();
        Set<String> ukjenteIdenter = new HashSet<>(identListe);
        Map<String, String> identer = new HashMap<>();

        Set<String> gyldigeIdenter = sjekkOmGyldigeIdenter.execute(ukjenteIdenter);
        insertIntoMap(identer, gyldigeIdenter, GYLDIG);

        Set<String> ugyldigeIdenter = new HashSet<>(ukjenteIdenter);
        ugyldigeIdenter.removeAll(gyldigeIdenter);
        insertIntoMap(identer, ugyldigeIdenter, IKKE_GYLDIG);

        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(gyldigeIdenter);
        Set<String> ledigeIdenterMiljo = filterPaaIdenterTilgjengeligeIMiljo.filtrer(gyldigeIdenter);

        Set<String> ledigeIdenter = new HashSet<>();
        ledigeIdenter.addAll(ledigeIdenterMiljo);
        ledigeIdenter.retainAll(ledigeIdenterDB);

        insertIntoMap(identer, ledigeIdenter, LEDIG_OG_GYLDIG);

        Set<String> opptattIdenter = new HashSet<>(gyldigeIdenter);
        opptattIdenter.removeAll(ledigeIdenter);

        insertIntoMap(identer, opptattIdenter, IKKE_LEDIG);

        for(Map.Entry<String, String> entry : identer.entrySet()) {
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
