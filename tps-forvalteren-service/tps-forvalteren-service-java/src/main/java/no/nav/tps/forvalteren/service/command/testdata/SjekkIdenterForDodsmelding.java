package no.nav.tps.forvalteren.service.command.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.service.command.testdata.opprett.FindIdenterNotUsedInDB;
import no.nav.tps.forvalteren.service.command.testdata.response.IdentMedStatus;

@Service
public class SjekkIdenterForDodsmelding extends SjekkIdenter {

    @Autowired
    private SjekkOmGyldigeIdenter sjekkOmGyldigeIdenter;

    @Autowired
    private FindIdenterNotUsedInDB findIdenterNotUsedInDB;

    @Autowired
    private FiltrerPaaIdenterTilgjengeligeIMiljo filtrerPaaIdenterTilgjengeligeIMiljo;

    public Set<IdentMedStatus> finnGyldigeOgLedigeIdenterForDoedsmeldinger(List<DeathRow> doedsmeldinger) {
        Set<String> ukjenteIdenter = doedsmeldinger.stream().map(DeathRow::getIdent).collect(Collectors.toSet());
        Map<String, String> identerMedStatus = new HashMap<>();

        Set<String> gyldigeIdenter = sjekkOmGyldigeIdenter.execute(ukjenteIdenter);
        setStatusOnDifference(ukjenteIdenter, gyldigeIdenter, identerMedStatus, IKKE_GYLDIG);

        Set<String> ledigeIdenterDBOgMiljo = finnLedigeIdenterDBOgMiljoOgSetStatus(identerMedStatus, gyldigeIdenter, getEnvironmentFromDoedsmeldinger(doedsmeldinger));
        setStatusOnDifference(gyldigeIdenter, ledigeIdenterDBOgMiljo, identerMedStatus, IKKE_LEDIG);
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

    private String getEnvironmentFromDoedsmeldinger(List<DeathRow> doedsmeldinger) {
        String environment = doedsmeldinger.get(0).getMiljoe();
        doedsmeldinger.forEach(doedsmelding -> {
            if (!doedsmelding.getMiljoe().equals(environment)) {
                throw new RuntimeException("Dødsmeldinger må være for samme miljø");
            }
        });
        return environment;
    }
}