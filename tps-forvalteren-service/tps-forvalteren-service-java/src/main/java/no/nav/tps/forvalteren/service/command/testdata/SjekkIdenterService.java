package no.nav.tps.forvalteren.service.command.testdata;

import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.collect.Sets.newHashSetWithExpectedSize;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerOgSjekkMiljoeService.PROD_ENV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.service.IdentpoolService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.response.CheckIdentResponse;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentStatusExtended;

@Service
public class SjekkIdenterService {

    @Autowired
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Autowired
    private SjekkOmGyldigeIdenter sjekkOmGyldigeIdenter;

    @Autowired
    private IdentpoolService identpoolService;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligIMiljo filtrerPaaIdenterTilgjengeligIMiljo;

    private static final String IKKE_GYLDIG = "IG";
    private static final String IKKE_LEDIG = "IL";
    private static final String LEDIG_OG_GYLDIG = "LOG";
    private static final String GYLDIG_OG_LEDIG = "Gyldig og ledig";

    public Set<IdentMedStatus> finnGyldigeOgLedigeIdenter(List<String> identListe) {
        Set<String> ukjenteIdenter = new HashSet<>(identListe);
        Map<String, String> identerMedStatus = new HashMap<>();

        Set<String> gyldigeIdenter = sjekkOmGyldigeIdenter.execute(ukjenteIdenter);
        setStatusOnDifference(ukjenteIdenter, gyldigeIdenter, identerMedStatus, IKKE_GYLDIG);

        Set<String> ledigeIdenterDBOgMiljo = finnLedigeIdenterDBOgMiljoOgSetStatus(identerMedStatus, gyldigeIdenter);
        setStatusOnDifference(gyldigeIdenter, ledigeIdenterDBOgMiljo, identerMedStatus, IKKE_LEDIG);
        return mapToIdentMedStatusSet(identerMedStatus);
    }

    public CheckIdentResponse finnLedigeIdenter(List<String> identListe) {
        Set<String> ukjenteIdenter = new HashSet<>(identListe);
        Map<String, String> identerMedStatus = new HashMap<>();

        Set<String> gyldigeIdenter = sjekkOmGyldigeIdenter.execute(ukjenteIdenter);
        setStatusOnDifference(ukjenteIdenter, gyldigeIdenter, identerMedStatus, "Ikke gyldig ident");

        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(gyldigeIdenter);
        setStatusOnDifference(gyldigeIdenter, ledigeIdenterDB, identerMedStatus, "Ikke ledig -- ident finnes allerede i database");

        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(ledigeIdenterDB, newHashSet(PROD_ENV));
        Set<String> koorigerteLedigeIdenterIMiljo = identpoolService.whitelistAjustmentOfIdents(gyldigeIdenter, ledigeIdenterDB, ledigeIdenterMiljo);

        insertIntoMap(identerMedStatus, koorigerteLedigeIdenterIMiljo, GYLDIG_OG_LEDIG);

        setStatusOnDifference(ledigeIdenterDB, koorigerteLedigeIdenterIMiljo, identerMedStatus, "Ikke ledig -- ident finnes i prod");

        return CheckIdentResponse.builder()
                .statuser(setAvailibility(mapToIdentMedStatusSet(identerMedStatus)))
                .build();
    }

    private List<IdentStatusExtended> setAvailibility(Set<IdentMedStatus> identStatuser) {
        List<IdentStatusExtended> utvidetStatus = new ArrayList<>();
        identStatuser.forEach(identMedStatus -> utvidetStatus.add(IdentStatusExtended.builder()
                .ident(identMedStatus.getIdent())
                .status(identMedStatus.getStatus())
                .available(GYLDIG_OG_LEDIG.equals(identMedStatus.getStatus()))
                .build()));
        return utvidetStatus;
    }

    protected void setStatusOnDifference(Set<String> firstIdentSet, Set<String> secondIdentSet, Map<String, String> identerMedStatus, String status) {
        Set<String> identer = new HashSet<>(firstIdentSet);
        identer.removeAll(secondIdentSet);
        insertIntoMap(identerMedStatus, identer, status);
    }

    private Set<String> finnLedigeIdenterDBOgMiljoOgSetStatus(Map<String, String> identerMedStatus, Set<String> gyldigeIdenter) {
        Set<String> ledigeIdenterDB = findIdenterNotUsedInDB.filtrer(gyldigeIdenter);
        // Environment PRODLIKE only verified for existence
        Set<String> environments = newHashSet(PROD_ENV);
        Set<String> ledigeIdenterMiljo = filtrerPaaIdenterTilgjengeligIMiljo.filtrer(gyldigeIdenter, environments);

        Set<String> ledigeIdenterDBOgMiljo = new HashSet<>();
        ledigeIdenterDBOgMiljo.addAll(ledigeIdenterMiljo);
        ledigeIdenterDBOgMiljo.retainAll(ledigeIdenterDB);

        insertIntoMap(identerMedStatus, ledigeIdenterDBOgMiljo, LEDIG_OG_GYLDIG);
        return ledigeIdenterDBOgMiljo;
    }

    protected Set<IdentMedStatus> mapToIdentMedStatusSet(Map<String, String> identer) {

        Set<IdentMedStatus> identerMedStatus = newHashSetWithExpectedSize(identer.size());

        for (Map.Entry<String, String> entry : identer.entrySet()) {
            identerMedStatus.add(new IdentMedStatus(entry.getKey(), entry.getValue()));
        }
        return identerMedStatus;
    }

    protected void insertIntoMap(Map<String, String> identer, Set<String> gyldigeIdenter, String status) {
        for (String ident : gyldigeIdenter) {
            identer.put(ident, status);
        }
    }
}
