package no.nav.tps.forvalteren.service.command.testdata.restreq;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Relasjon;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsRestPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.service.RelasjonType;
import no.nav.tps.forvalteren.service.command.testdata.SavePersonBulk;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersoner;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetNameOnPersonsService;
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
    private SetNameOnPersonsService setNameOnPersonsService;

    @Autowired
    private OpprettPersoner opprettPersonerFraIdenter;

    @Autowired
    private SavePersonBulk savePersonBulk;

    @Autowired
    private ExtractOpprettKritereFromDollyKriterier extractOpprettKritereFromDollyKriterier;

    public List<Person> createTpsfPersonFromRestRequest(RsRestPersonKriteriumRequest personKriteriumRequest){
        RsPersonKriteriumRequest personKriterier = extractOpprettKritereFromDollyKriterier.execute(personKriteriumRequest);
        RsPersonKriteriumRequest kriteriePartner = extractOpprettKritereFromDollyKriterier.extractPartner(personKriteriumRequest.getRelasjoner());
        RsPersonKriteriumRequest kriterieBarn = extractOpprettKritereFromDollyKriterier.extractBarn(personKriteriumRequest.getRelasjoner());

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

    private boolean harPartner(RsRestPersonKriteriumRequest request){
        return request.getRelasjoner() != null && request.getRelasjoner().getPartner() != null;
    }

    private boolean harBarn(RsRestPersonKriteriumRequest request){
        return request.getRelasjoner() != null && request.getRelasjoner().getBarn() != null && !request.getRelasjoner().getBarn().isEmpty();
    }

    public List<Person> convertRequestTilPersoner(RsPersonKriteriumRequest personKriterierListe){
        List<TestdataRequest> testdataRequests = testdataIdenterFetcher.getTestdataRequestsInnholdeneTilgjengeligeIdenter(personKriterierListe);

        List<String> identer = ekstraherIdenterFraTestdataRequests.execute(testdataRequests);
        List<Person> personerSomSkalPersisteres = opprettPersonerFraIdenter.execute(identer);

        setNameOnPersonsService.execute(personerSomSkalPersisteres);

        return personerSomSkalPersisteres;
    }

    public void setRelasjonerPaaPersoner(List<Person> personer, List<Person> partnerListe, List<Person> barn){
        int antallbarn = barn.isEmpty() ? 0 : barn.size() / personer.size();

        for(int i = 0; i < personer.size(); i++){
            Person person = personer.get(i);
            person.setRelasjoner(new ArrayList<>());
            Person partner = null;

            if(partnerListe != null && !partnerListe.isEmpty()){
                partner = partnerListe.get(i);
                partner.setRelasjoner(new ArrayList<>());

                Relasjon partnerRelasjon = lagPartnerRelasjon(person, partner);
                Relasjon personRelasjon = lagPartnerRelasjon(partner, person);

                person.getRelasjoner().add(partnerRelasjon);
                partner.getRelasjoner().add(personRelasjon);
            }

            for(int j = 0; j < antallbarn; j++){
                int startIndexBarn = i*antallbarn;
                Person barnet = barn.get(startIndexBarn +j);
                Relasjon barnRelasjon = new Relasjon();
                Relasjon morRelasjon = new Relasjon();

                Person mor = getMother(person, partner);

                if(mor == null){
                    throw new IllegalArgumentException("Kan ikke lage fødselsmelding uten en mor");
                }

                barnRelasjon.setPerson(barnet);
                barnRelasjon.setPersonRelasjonMed(mor);
                barnRelasjon.setRelasjonTypeNavn(RelasjonType.FOEDSEL.getRelasjonTypeNavn());
                barnet.getRelasjoner().add(barnRelasjon);

                morRelasjon.setPerson(mor);
                morRelasjon.setPersonRelasjonMed(barnet);
                morRelasjon.setRelasjonTypeNavn(RelasjonType.MOR.getRelasjonTypeNavn());
                mor.getRelasjoner().add(morRelasjon);

                Person far;
                if(person == mor){
                    far = partner;
                } else {
                    far = person;
                }

                if(far != null){
                    Relasjon farRelasjon = new Relasjon();
                    farRelasjon.setPerson(far);
                    farRelasjon.setPersonRelasjonMed(barnet);

                    if(far.getKjonn() == 'M'){
                        farRelasjon.setRelasjonTypeNavn(RelasjonType.FAR.getRelasjonTypeNavn());
                    } else {
                        farRelasjon.setRelasjonTypeNavn(RelasjonType.MOR.getRelasjonTypeNavn());
                    }

                    far.getRelasjoner().add(farRelasjon);
                }
            }
        }
    }

    private Relasjon lagPartnerRelasjon(Person person, Person partner){
        Relasjon partnerRelasjon = new Relasjon();
        partnerRelasjon.setPerson(person);
        partnerRelasjon.setPersonRelasjonMed(partner);
        partnerRelasjon.setRelasjonTypeNavn(RelasjonType.EKTEFELLE.getRelasjonTypeNavn());

        return partnerRelasjon;
    }


    private Person getMother(Person p1, Person p2){
        if(p1 != null && p1.getKjonn() == 'K'){
            return p1;
        }

        if(p2 != null && p2.getKjonn() == 'K'){
            return p2;
        }

        return null;
    }

    private Person getFar(Person p1, Person p2){
        if(p1 != null && p1.getKjonn() == 'K'){
            return p1;
        }

        if(p2 != null && p2.getKjonn() == 'K'){
            return p2;
        }

        return null;
    }
}
