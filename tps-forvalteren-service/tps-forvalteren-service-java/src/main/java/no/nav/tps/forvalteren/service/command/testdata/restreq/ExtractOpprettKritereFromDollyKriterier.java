package no.nav.tps.forvalteren.service.command.testdata.restreq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleDollyRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetDummyAdresseOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Service
public class ExtractOpprettKritereFromDollyKriterier {

    @Autowired
    private SetDummyAdresseOnPersons setDummyAdresseOnPersons;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdent;

    @Autowired
    private MapperFacade mapperFacade;

    public RsPersonKriteriumRequest execute(RsPersonBestillingKriteriumRequest req) {
        RsPersonKriterier rsPerson = new RsPersonKriterier();
        rsPerson.setAntall(req.getAntall());
        rsPerson.setIdenttype(req.getIdenttype());
        rsPerson.setKjonn(req.getKjonn());
        rsPerson.setFoedtEtter(req.getFoedtEtter());
        rsPerson.setFoedtFoer(req.getFoedtFoer());
        rsPerson.setSivilstand(req.getSivilstand());

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
                for (RsSimpleDollyRequest req : rel.getBarn()) {
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

    public List<Person> addDollyKriterumValuesToPerson(RsPersonBestillingKriteriumRequest req, List<Person> personer) {
        personer.forEach(person -> {
                    person.setRegdato(req.getRegdato());
                    person.setDoedsdato(req.getDoedsdato());
                    person.setStatsborgerskap(req.getStatsborgerskap() != null ? req.getStatsborgerskap() : "NOR");
                    person.setStatsborgerskapRegdato(hentDatoFraIdent.extract(person.getIdent()));
                    person.setTypeSikkerhetsTiltak(req.getTypeSikkerhetsTiltak());
                    person.setSikkerhetsTiltakDatoFom(req.getSikkerhetsTiltakDatoFom());
                    person.setSikkerhetsTiltakDatoTom(req.getSikkerhetsTiltakDatoTom());
                    person.setSpesreg(req.getSpesreg());
                    person.setSivilstand(req.getSivilstand());
                    person.setSpesregDato(req.getSpesregDato());
                    person.setEgenAnsattDatoFom(req.getEgenAnsattDatoFom());
                    person.setEgenAnsattDatoTom(req.getEgenAnsattDatoTom());

                    if (req.getBoadresse() != null) {
                        person.setBoadresse(mapperFacade.map(req.getBoadresse(), Adresse.class));
                        if (person.getBoadresse().getFlyttedato() == null) {
                            person.getBoadresse().setFlyttedato(hentDatoFraIdent.extract(person.getIdent()));
                        }
                        person.getBoadresse().setPerson(person);
                    }

                    if (req.getPostadresse() != null && !req.getPostadresse().isEmpty()) {
                        person.setPostadresse(mapperFacade.mapAsList(req.getPostadresse(), Postadresse.class));
                        person.getPostadresse().forEach(adr -> adr.setPerson(person));
                    }
                }
        );

        if (!hasGateAdresse(req)) {
            setDummyAdresseOnPersons.execute(personer);
        }

        return personer;
    }

    private boolean hasGateAdresse(RsPersonBestillingKriteriumRequest req) {
        return req.getBoadresse() != null;
    }
}
