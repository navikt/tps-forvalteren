package no.nav.tps.forvalteren.service.command.testdata.restreq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;

@Service
public class ExtractOpprettKriterier {

    @Autowired
    private MapperFacade mapperFacade;

    public RsPersonKriteriumRequest execute(RsPersonBestillingKriteriumRequest req) {
        RsPersonKriterier rsPerson = new RsPersonKriterier();
        rsPerson.setAntall(req.getAntall());
        rsPerson.setIdenttype(req.getIdenttype());
        rsPerson.setKjonn(req.getKjonn());
        rsPerson.setFoedtEtter(req.getFoedtEtter());
        rsPerson.setFoedtFoer(req.getFoedtFoer());

        RsPersonKriteriumRequest kriteriumRequest = new RsPersonKriteriumRequest();
        kriteriumRequest.setPersonKriterierListe(Arrays.asList(rsPerson));

        return kriteriumRequest;
    }

    public RsPersonKriteriumRequest extractPartner(RsPersonBestillingKriteriumRequest request) {
        RsSimpleRelasjoner rel = request.getRelasjoner();
        RsPersonKriteriumRequest personRequestListe = new RsPersonKriteriumRequest();
        if (rel != null && rel.getPartner() != null) {
            RsPersonKriterier partnerReq = new RsPersonKriterier();
            partnerReq.setAntall(request.getAntall());

            partnerReq.setIdenttype(getIdenttype(rel.getPartner().getIdenttype()));

            partnerReq.setKjonn(rel.getPartner().getKjonn());
            partnerReq.setFoedtFoer(rel.getPartner().getFoedtFoer());
            partnerReq.setFoedtEtter(rel.getPartner().getFoedtEtter());
            personRequestListe.setPersonKriterierListe(Arrays.asList(partnerReq));
        }

        return personRequestListe;
    }

    private String getIdenttype(String identype) {
        return identype != null ? identype : "FNR";
    }

    public RsPersonKriteriumRequest extractBarn(RsPersonBestillingKriteriumRequest request) {
        RsSimpleRelasjoner rel = request.getRelasjoner();
        RsPersonKriteriumRequest personRequestListe = new RsPersonKriteriumRequest();
        personRequestListe.setPersonKriterierListe(new ArrayList<>());
        if (harBarn(request)) {

            for (int i = 0; i < request.getAntall(); i++) {
                for (RsSimplePersonRequest req : rel.getBarn()) {
                    RsPersonKriterier barnKriterie = new RsPersonKriterier();
                    barnKriterie.setAntall(1);

                    barnKriterie.setIdenttype(getIdenttype(req.getIdenttype()));

                    barnKriterie.setKjonn(req.getKjonn());
                    barnKriterie.setFoedtFoer(req.getFoedtFoer());
                    barnKriterie.setFoedtEtter(req.getFoedtEtter());

                    personRequestListe.getPersonKriterierListe().add(barnKriterie);
                }
            }
        }

        return personRequestListe;
    }

    private boolean harBarn(RsPersonBestillingKriteriumRequest request) {
        RsSimpleRelasjoner rel = request.getRelasjoner();
        return rel != null && (rel.getBarn() != null && !rel.getBarn().isEmpty());
    }

    public List<Person> addExtendedKriterumValuesToPerson(RsPersonBestillingKriteriumRequest req, List<Person> hovedPersoner, List<Person> partnere, List<Person> barn) {

        hovedPersoner.forEach(person -> mapperFacade.map(req, person));
        partnere.forEach(partner -> {
                    mapperFacade.map(req, partner);
                    mapPersonAttributes(req.getRelasjoner().getPartner(), partner);
                }
        );
        IntStream.range(0, barn.size()).forEach(i -> {
            mapperFacade.map(req, barn.get(i));
            mapPersonAttributes(req.getRelasjoner().getBarn().get(i), barn.get(i));
        });

        List<Person> personer = new ArrayList<>();
        Stream.of(hovedPersoner, partnere, barn).forEach(personer::addAll);
        return personer;
    }

    private Person mapPersonAttributes(RsSimplePersonRequest kriterier, Person person) {
        person.setStatsborgerskap(kriterier.getStatsborgerskap() != null ?
                kriterier.getStatsborgerskap() : person.getStatsborgerskap());
        person.setStatsborgerskapRegdato(kriterier.getStatsborgerskapRegdato() != null ?
                kriterier.getStatsborgerskapRegdato() : person.getStatsborgerskapRegdato());
        person.setSprakKode(kriterier.getSprakKode() != null ?
                kriterier.getSprakKode() : person.getSprakKode());
        person.setDatoSprak(kriterier.getDatoSprak() != null ?
                kriterier.getDatoSprak() : person.getDatoSprak());
        return person;
    }
}