package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterierListe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpprettTestdataPersoner {

    @Autowired
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    @Autowired
    private FilterPaaIdenterTilgjengeligeIMiljo filterPaaIdenterTilgjengeligeIMiljo;

    @Autowired
    private SetNameOnPersonsService setNameOnPersonsService;

    @Autowired
    private SavePersonListService SavePersonListService;

    public void opprettPersoner(RsPersonKriterierListe personKriterierListe) {

        List<List<String>> alleMuligeIdenter = genererMuligeIdenter(personKriterierListe);
        List<List<String>> alleGyldigeIdenter = tilgjengligeIdenter(alleMuligeIdenter);

        int genererOgSjekkNyeIdenterCounter = 0;
        List<Integer> kriterierSomManglerTilgjengligeIdenter = manglerTilgjengligeIdenter(personKriterierListe, alleGyldigeIdenter);
        while (!kriterierSomManglerTilgjengligeIdenter.isEmpty() && genererOgSjekkNyeIdenterCounter < 10) {
            manglerTilgjengligeIdenter(personKriterierListe, alleGyldigeIdenter);
            genererOgSjekkNyeIdenterCounter++;
        }

        List<Person> personer = opprettPersonObjekter(personKriterierListe, alleGyldigeIdenter);
        setNameOnPersonsService.execute(personer);
        SavePersonListService.save(personer);

    }

    public List<Person> opprettPersonObjekter(RsPersonKriterierListe personKriterierListe, List<List<String>> alleGyldigeIdenter) {
        List<Person> personer = new ArrayList<>();

        for (int kriterieIndex = 0; kriterieIndex < personKriterierListe.getPersonKriterierListe().size(); kriterieIndex++) {
            RsPersonKriterier kriterie = personKriterierListe.getPersonKriterierListe().get(kriterieIndex);

            for (int kriterieAntallIndex = 0; kriterieAntallIndex < kriterie.getAntall(); kriterieAntallIndex++) {
                Person newPerson = new Person();
                newPerson.setIdenttype(kriterie.getIdenttype());
                newPerson.setIdent(alleGyldigeIdenter.get(kriterieIndex).get(kriterieAntallIndex));
                newPerson.setKjonn(kriterie.getKjonn());
                newPerson.setRegdato(LocalDateTime.now());
                personer.add(newPerson);
            }
        }
        return personer;
    }

    public List<Integer> manglerTilgjengligeIdenter(RsPersonKriterierListe personKriterierListe, List<List<String>> alleGyldigeIdenter) {
        List<Integer> kriterierSomManglerIdenter = sjekkOmNokGyldigeIdenter(personKriterierListe, alleGyldigeIdenter);

        for (Integer kriterieIndex : kriterierSomManglerIdenter) {
            List<String> muligeIdenter = fiktiveIdenterGenerator.genererFiktiveIdenter(personKriterierListe.getPersonKriterierListe().get(kriterieIndex));
            List<String> gyldigeIdenter = filterPaaIdenterTilgjengeligeIMiljo.filtrer(muligeIdenter);
            alleGyldigeIdenter.get(kriterieIndex).addAll(gyldigeIdenter);
        }
        return kriterierSomManglerIdenter;
    }

    public List<List<String>> genererMuligeIdenter(RsPersonKriterierListe personKriterierListe) {
        List<List<String>> alleMuligeIdenter = new ArrayList<>();
        for (RsPersonKriterier kriterie : personKriterierListe.getPersonKriterierListe()) {
            List<String> identer = fiktiveIdenterGenerator.genererFiktiveIdenter(kriterie);
            alleMuligeIdenter.add(identer);
        }
        return alleMuligeIdenter;
    }

    public List<List<String>> tilgjengligeIdenter(List<List<String>> alleMuligeIdenter) {
        List<List<String>> alleGyldigeIdenter = new ArrayList<>();
        for (List<String> muligeIdenter : alleMuligeIdenter) {
            List<String> gyldigeIdenter = filterPaaIdenterTilgjengeligeIMiljo.filtrer(muligeIdenter);
            alleGyldigeIdenter.add(gyldigeIdenter);
        }
        return alleGyldigeIdenter;
    }

    public List<Integer> sjekkOmNokGyldigeIdenter(RsPersonKriterierListe personKriterierListe, List<List<String>> alleGyldigeIdenter) {

        List<Integer> kriterierSomManglerIdenter = new ArrayList<>();

        for (int kriterieIndex = 0; kriterieIndex < personKriterierListe.getPersonKriterierListe().size(); kriterieIndex++) {
            int personKriterierSize = personKriterierListe.getPersonKriterierListe().get(kriterieIndex).getAntall();
            int gyldigeIdenterSize = alleGyldigeIdenter.get(kriterieIndex).size();
            if (personKriterierSize > gyldigeIdenterSize) {
                kriterierSomManglerIdenter.add(kriterieIndex);
            }
        }
        return kriterierSomManglerIdenter;
    }

}
