package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterieRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentKjoennFraIdent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OpprettTestdataPersoner {

    private static final int MAX_TRIES = 20;

    @Autowired
    private HentKjoennFraIdent hentKjoennFraIdent;

    @Autowired
    private FiktiveIdenterGenerator fiktiveIdenterGenerator;

    @Autowired
    private FilterPaaIdenterTilgjengeligeIMiljo filterPaaIdenterTilgjengeligeIMiljo;

    public List<Person> hentIdenterSomSkalBliPersoner(RsPersonKriterieRequest personKriterierRequest) {
        Map<Integer, Set<String>> kriterierNummerert = genererIdenterForAlleKriterier(personKriterierRequest);
        taBortOpptatteIdenter(kriterierNummerert);

        if (!erAlleKriterieOppfylt(kriterierNummerert, personKriterierRequest)) {
            oppdaterMapMedIdenterTilManglendeKriterier(kriterierNummerert, personKriterierRequest);
        }

        return opprettPersonerBasertPaaLedigeIdenter(kriterierNummerert, personKriterierRequest);
    }

    private Map<Integer, Set<String>> genererIdenterForAlleKriterier(RsPersonKriterieRequest kriterierRequest) {
        Map<Integer, Set<String>> kritererMap = new HashMap<>();
        for (int i = 0; i < kriterierRequest.getPersonKriterierListe().size(); i++) {
            Set<String> identerForKritere = fiktiveIdenterGenerator.genererFiktiveIdenter(kriterierRequest.getPersonKriterierListe().get(i));
            taBortIdenterLagtTilIAndreKriterier(kritererMap, identerForKritere);
            kritererMap.put(i, identerForKritere);
        }
        return kritererMap;
    }

    private void taBortOpptatteIdenter(Map<Integer, Set<String>> identMap) {
        Set<String> alleGenererteIdenter = new HashSet<>();
        for (Map.Entry<Integer, Set<String>> fnrs : identMap.entrySet()) {
            alleGenererteIdenter.addAll(fnrs.getValue());
        }
        Set<String> alleTilgjengeligIdenter = filterPaaIdenterTilgjengeligeIMiljo.filtrer(alleGenererteIdenter);
        taBortOpptatteIdenterFraMap(identMap, alleTilgjengeligIdenter);
    }


    private void taBortIdenterLagtTilIAndreKriterier(Map<Integer, Set<String>> identMap, Set<String> identerForKritere) {
        for (String ident : new HashSet<>(identerForKritere)) {
            for(int i = 0; i<identMap.size(); i++){
                if(identMap.get(i).contains(ident)){
                    identerForKritere.remove(ident);
                }
            }
        }
    }

    private void taBortOpptatteIdenterFraMap(Map<Integer, Set<String>> identMap, Set<String> alleTilgjengligIdenter) {
        for (int i = 0; i < identMap.size(); i++) {
            identMap.get(i).retainAll(alleTilgjengligIdenter);
        }
    }

    private List<Person> opprettPersonerBasertPaaLedigeIdenter(Map<Integer, Set<String>> identMap, RsPersonKriterieRequest personKriterierRequest) {
        List<Person> personerSomSkalPersisteres = new ArrayList<>();
        for (int i = 0; i < identMap.size(); i++) {
            List<Person> personer = opprettPersoner(identMap.get(i), personKriterierRequest.getPersonKriterierListe().get(i));
            personerSomSkalPersisteres.addAll(personer);
        }
        return personerSomSkalPersisteres;
    }

    private List<Person> opprettPersoner(Set<String> tilgjengeligIdenter, RsPersonKriterier kriterie) {
        List<Person> personer = new ArrayList<>();
        for (String ident : tilgjengeligIdenter) {
            Person newPerson = new Person();
            newPerson.setIdenttype(kriterie.getIdenttype());
            newPerson.setIdent(ident);
            newPerson.setKjonn(hentKjoennFraIdent.execute(ident));
            newPerson.setRegdato(LocalDateTime.now());
            personer.add(newPerson);
            if (kriterie.getAntall() == personer.size()) {
                break;
            }
        }
        return personer;
    }

    private void oppdaterMapMedIdenterTilManglendeKriterier(Map<Integer, Set<String>> identMap, RsPersonKriterieRequest personKriterierRequest) {
        for (int i = 0; i < identMap.size(); i++) {
            if (!harNokIdenterForKritere(personKriterierRequest.getPersonKriterierListe().get(i), identMap.get(i).size())) {
                int counter = 0;
                while ((counter < MAX_TRIES) && !harNokIdenterForKritere(personKriterierRequest.getPersonKriterierListe().get(i), identMap.get(i).size())) {
                    RsPersonKriterieRequest singelKriterieListe = new RsPersonKriterieRequest();
                    singelKriterieListe.setPersonKriterierListe(new ArrayList<>());
                    singelKriterieListe.getPersonKriterierListe().add(personKriterierRequest.getPersonKriterierListe().get(i));
                    Map<Integer, Set<String>> nyeIdenterMap = genererIdenterForAlleKriterier(singelKriterieListe);
                    identMap.get(i).addAll(nyeIdenterMap.get(0));
                    counter++;
                }
                //TODO Throw exception hvis ikk går på 5 forsøk.
            }
        }
    }

    private boolean erAlleKriterieOppfylt(Map<Integer, Set<String>> identMap, RsPersonKriterieRequest personKriterierRequest) {
        for (int i = 0; i < identMap.size(); i++) {
            if (!harNokIdenterForKritere(personKriterierRequest.getPersonKriterierListe().get(i), identMap.get(i).size())) {
                return false;
            }
        }
        return true;
    }

    private boolean harNokIdenterForKritere(RsPersonKriterier kriterier, int antallIdentManHar) {
        return antallIdentManHar >= kriterier.getAntall();
    }


}

