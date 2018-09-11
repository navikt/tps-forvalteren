package no.nav.tps.forvalteren.service.command.testdata.restreq;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.rs.RsRestPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleRelasjoner;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimpleDollyRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetDummyAdresseOnPersons;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtractOpprettKritereFromDollyKriterier {

    @Autowired
    private SetDummyAdresseOnPersons setDummyAdresseOnPersons;

    @Autowired
    private MapperFacade mapperFacade;

    public RsPersonKriteriumRequest execute(RsRestPersonKriteriumRequest req){
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

    public RsPersonKriteriumRequest extractPartner(RsSimpleRelasjoner rel){
        RsPersonKriteriumRequest personRequestListe = new RsPersonKriteriumRequest();
        if(rel != null && rel.getPartner() != null){
            RsPersonKriterier partnerReq = new RsPersonKriterier();
            partnerReq.setAntall(1);

            if(partnerReq.getIdenttype() != null){
                partnerReq.setIdenttype(rel.getPartner().getIdenttype()); // BAre for teste
            } else {
                partnerReq.setIdenttype("FNR");
            }

            partnerReq.setKjonn(rel.getPartner().getKjonn());
            partnerReq.setFoedtFoer(rel.getPartner().getFoedtFoer());
            partnerReq.setFoedtEtter(rel.getPartner().getFoedtEtter());
            personRequestListe.setPersonKriterierListe(Arrays.asList(partnerReq));
        }

        return personRequestListe;
    }

    public RsPersonKriteriumRequest extractBarn(RsSimpleRelasjoner rel){
        RsPersonKriteriumRequest personRequestListe = new RsPersonKriteriumRequest();
        if(rel != null && rel != null){
            for(RsSimpleDollyRequest req : rel.getBarn()){
                RsPersonKriterier partnerReq = new RsPersonKriterier();
                partnerReq.setAntall(1);

                if(partnerReq.getIdenttype() != null){
                    partnerReq.setIdenttype(req.getIdenttype()); // BAre for teste
                } else {
                    partnerReq.setIdenttype("FNR");
                }

                partnerReq.setKjonn(req.getKjonn());
                partnerReq.setFoedtFoer(req.getFoedtFoer());
                partnerReq.setFoedtEtter(req.getFoedtEtter());
                personRequestListe.setPersonKriterierListe(Arrays.asList(partnerReq));
            }
        }

        return personRequestListe;
    }

    public List<Person> addDollyKriterumValuesToPersonAndSave(RsRestPersonKriteriumRequest req, List<Person> personer){
        personer.forEach(person -> {
                    person.setRegdato(req.getRegdato());
                    person.setDoedsdato(req.getDoedsdato());
                    person.setStatsborgerskap(req.getStatsborgerskap());
                    person.setTypeSikkerhetsTiltak(req.getTypeSikkerhetsTiltak());
                    person.setSikkerhetsTiltakDatoFom(req.getSikkerhetsTiltakDatoFom());
                    person.setSikkerhetsTiltakDatoTom(req.getSikkerhetsTiltakDatoTom());
                    person.setSpesreg(req.getSpesreg());
                    person.setSpesregDato(req.getSpesregDato());
                    person.setEgenAnsattDatoFom(req.getEgenAnsattDatoFom());
                    person.setEgenAnsattDatoTom(req.getEgenAnsattDatoTom());

                    if(req.getBoadresse() != null){
                        person.setBoadresse(mapperFacade.map(req.getBoadresse(), Adresse.class));
                        person.getBoadresse().setPerson(person);
                    }

                    if(req.getPostadresse() != null && !req.getPostadresse().isEmpty()){
                        person.setPostadresse(mapperFacade.mapAsList(req.getPostadresse(), Postadresse.class));
                        person.getPostadresse().forEach(adr -> adr.setPerson(person));
                    }
                }
        );

        if(!hasGateAdresse(req)){
            setDummyAdresseOnPersons.execute(personer);
        }

        return personer;
    }

    private boolean hasGateAdresse(RsRestPersonKriteriumRequest req){
        return req.getBoadresse() != null;
    }
}
