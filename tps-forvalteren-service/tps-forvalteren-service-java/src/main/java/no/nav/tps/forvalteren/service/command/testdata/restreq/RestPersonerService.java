package no.nav.tps.forvalteren.service.command.testdata.restreq;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsRestPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonBulk;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataRequest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestPersonerService {

    @Autowired
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Autowired
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Autowired
    private PersonNameService setNameOnPersonsService;

    @Autowired
    private OpprettPersonerService opprettPersonerFraIdenter;

    @Autowired
    private SavePersonBulk savePersonBulk;

    @Autowired
    private ExtractOpprettKritereFromDollyKriterier extractOpprettKritereFromDollyKriterier;

    public List<Person> createTpsfPersonFromRestRequest(RsRestPersonKriteriumRequest personKriteriumRequest){
        RsPersonKriteriumRequest personKriterier = extractOpprettKritereFromDollyKriterier.execute(personKriteriumRequest);
        RsPersonKriteriumRequest kriteriePartner = extractOpprettKritereFromDollyKriterier.extractPartner(personKriteriumRequest);
        RsPersonKriteriumRequest kriterieBarn = extractOpprettKritereFromDollyKriterier.extractBarn(personKriteriumRequest);

        List<Person> deresPartnere = new ArrayList<>();
        List<Person> deresBarn = new ArrayList<>();
        List<Person> opprettedePersoner = savePersonBulk.execute(convertRequestTilPersoner(personKriterier));

        if(harPartner(personKriteriumRequest)){
            deresPartnere = savePersonBulk.execute(convertRequestTilPersoner(kriteriePartner));
        }
        if (harBarn(personKriteriumRequest)){
            deresBarn = savePersonBulk.execute(convertRequestTilPersoner(kriterieBarn));
        }

        setRelasjonerPaaPersoner(opprettedePersoner, deresPartnere, deresBarn);

        List<Person> personerSomSkalPersisteres = new ArrayList<>(opprettedePersoner);
        personerSomSkalPersisteres.addAll(deresPartnere);
        personerSomSkalPersisteres.addAll(deresBarn);

        List<Person> tpsfPersoner = extractOpprettKritereFromDollyKriterier.addDollyKriterumValuesToPersonAndSave(personKriteriumRequest, personerSomSkalPersisteres);

        return savePersonBulk.execute(tpsfPersoner);
    }

    public List<Person> convertRequestTilPersoner(RsPersonKriteriumRequest personKriterierListe){
        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(personKriterierListe);

        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.execute(identer);

        setNameOnPersonsService.execute(personerSomSkalPersisteres);

        return personerSomSkalPersisteres;
    }

    public void setRelasjonerPaaPersoner(List<Person> personer, List<Person> partnerListe, List<Person> barn){
        int antallbarn = (barn == null || barn.isEmpty()) ? 0 : barn.size() / personer.size();

        for(int i = 0; i < personer.size(); i++){
            Person person = personer.get(i);
            person.setRelasjoner(new ArrayList<>());
            Person partner = null;

            if(partnerListe != null && !partnerListe.isEmpty()){
                partner = partnerListe.get(i);
                partner.setRelasjoner(new ArrayList<>());

                lagPartnerRelasjon(person, partner);
                lagPartnerRelasjon(partner, person);
            }

            for(int j = 0; j < antallbarn; j++){
                int startIndexBarn = i*antallbarn;
                Person barnet = barn.get(startIndexBarn +j);

                setBarnRelasjon(person, barnet);
                setBarnRelasjon(partner, barnet);
            }
        }
    }

    private void setBarnRelasjon(Person forelder, Person barn){
        if(forelder == null) {
            return;
        }

        if(forelder.getKjonn() == 'M'){
            setFarBarnRelasjonMedInnvadring(forelder, barn);
        }

        if(forelder.getKjonn() == 'K'){
            setMorBarnRelasjonMedFodsel(forelder, barn);
        }
    }

    private void setFarBarnRelasjonMedInnvadring(Person far, Person barn){
        Relasjon barnRelasjon = new Relasjon();
        Relasjon farRelasjon = new Relasjon();

        barnRelasjon.setPersonRelasjonMed(far);
        barnRelasjon.setRelasjonTypeNavn(RelasjonType.BARN.getRelasjonTypeNavn());
        barnRelasjon.setPerson(barn);
        barn.getRelasjoner().add(barnRelasjon);

        farRelasjon.setPerson(far);
        farRelasjon.setPersonRelasjonMed(barn);
        farRelasjon.setRelasjonTypeNavn(RelasjonType.FAR.getRelasjonTypeNavn());
        far.getRelasjoner().add(farRelasjon);
    }

    private void setMorBarnRelasjonMedFodsel(Person mor, Person barn){
        Relasjon barnRelasjon = new Relasjon();
        Relasjon morRelasjon = new Relasjon();

        barnRelasjon.setPersonRelasjonMed(mor);
        barnRelasjon.setRelasjonTypeNavn(RelasjonType.FOEDSEL.getRelasjonTypeNavn());
        barnRelasjon.setPerson(barn);
        barn.getRelasjoner().add(barnRelasjon);

        morRelasjon.setPerson(mor);
        morRelasjon.setPersonRelasjonMed(barn);
        morRelasjon.setRelasjonTypeNavn(RelasjonType.MOR.getRelasjonTypeNavn());
        mor.getRelasjoner().add(morRelasjon);
    }

    private boolean harPartner(RsRestPersonKriteriumRequest request){
        return request.getRelasjoner() != null && request.getRelasjoner().getPartner() != null;
    }

    private boolean harBarn(RsRestPersonKriteriumRequest request){
        return request.getRelasjoner() != null && request.getRelasjoner().getBarn() != null && !request.getRelasjoner().getBarn().isEmpty();
    }

    private void lagPartnerRelasjon(Person person, Person partner){
        Relasjon partnerRelasjon = new Relasjon();
        partnerRelasjon.setPerson(person);
        partnerRelasjon.setPersonRelasjonMed(partner);
        partnerRelasjon.setRelasjonTypeNavn(RelasjonType.EKTEFELLE.getRelasjonTypeNavn());
        person.getRelasjoner().add(partnerRelasjon);
    }
}
