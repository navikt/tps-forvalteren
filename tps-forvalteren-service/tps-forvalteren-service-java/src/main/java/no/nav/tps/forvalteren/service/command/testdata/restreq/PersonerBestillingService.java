package no.nav.tps.forvalteren.service.command.testdata.restreq;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonBestillingKriteriumRequest;
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
public class PersonerBestillingService {

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

    public List<Person> createTpsfPersonFromRestRequest(RsPersonBestillingKriteriumRequest personKriteriumRequest){
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
            }

            for(int j = 0; j < antallbarn; j++){
                int startIndexBarn = i*antallbarn;
                Person barnet = barn.get(startIndexBarn +j);

                //TODO Sette fodsel paa begge hvis fodselsmelding.
                setBarnRelasjon(person, partner, barnet);
            }
        }
    }

    private void setBarnRelasjon(Person forelder, Person barn){
        if(forelder == null) {
            return;
        }

        if(forelder.getKjonn().equals("M")){
            setFarBarnRelasjonMedInnvadring(forelder, barn);
        }

        if(forelder.getKjonn().equals("K")){
            setMorBarnRelasjonMedFodsel(forelder, barn);
        }
    }

    private void setBarnRelasjon(Person person, Person partner, Person barn){
        if(partner == null){
            setBarnRelasjon(person, barn);
            return;
        }

        if(erMotsattKjonn(person, partner)){
            if(erKvinne(person)){
                setMorBarnRelasjonMedFodsel(person, barn);
                setFarBarnRelasjonMedFodsel(partner, barn);
            } else {
                setMorBarnRelasjonMedFodsel(partner, barn);
                setFarBarnRelasjonMedFodsel(person, barn);
            }
        } else if(erToKvinner(person, partner)){
            setMorBarnRelasjonMedFodsel(person, barn);
            setFarBarnRelasjonMedInnvadring(partner, barn);
        } else {
            setFarBarnRelasjonMedInnvadring(person, barn);
            setFarBarnRelasjonMedInnvadring(partner, barn);
        }
    }

    private void setFarBarnRelasjonMedInnvadring(Person far, Person barn){
        far.getRelasjoner().add(new Relasjon(far, barn, RelasjonType.BARN.getName()));
        barn.getRelasjoner().add(new Relasjon(barn, far, RelasjonType.FAR.getName()));
    }

    private void setMorBarnRelasjonMedFodsel(Person mor, Person barn){
        mor.getRelasjoner().add(new Relasjon(mor, barn, RelasjonType.FOEDSEL.getName()));
        barn.getRelasjoner().add(new Relasjon(barn, mor, RelasjonType.MOR.getName()));
    }

    private void setFarBarnRelasjonMedFodsel(Person mor, Person barn){
        mor.getRelasjoner().add(new Relasjon(mor, barn, RelasjonType.FOEDSEL.getName()));
        barn.getRelasjoner().add(new Relasjon(barn, mor, RelasjonType.FAR.getName()));
    }

    private boolean harPartner(RsPersonBestillingKriteriumRequest request){
        return request.getRelasjoner() != null && request.getRelasjoner().getPartner() != null;
    }

    private boolean harBarn(RsPersonBestillingKriteriumRequest request){
        return request.getRelasjoner() != null && request.getRelasjoner().getBarn() != null && !request.getRelasjoner().getBarn().isEmpty();
    }

    private boolean erMotsattKjonn(Person person, Person partner){
        return person.getKjonn() != partner.getKjonn();
    }

    private boolean erToKvinner(Person person, Person partner){
        return person.getKjonn().equals("K") && partner.getKjonn().equals("K");
    }

    private boolean erKvinne(Person person){
        return person.getKjonn().equals("K");
    }

    private void lagPartnerRelasjon(Person person, Person partner){
        person.getRelasjoner().add(new Relasjon(person, partner, RelasjonType.EKTEFELLE.getName()));
        partner.getRelasjoner().add(new Relasjon(partner, person, RelasjonType.EKTEFELLE.getName()));
    }
}