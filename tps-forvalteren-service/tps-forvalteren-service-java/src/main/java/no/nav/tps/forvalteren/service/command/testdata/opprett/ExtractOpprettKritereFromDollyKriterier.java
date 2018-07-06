package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsDollyPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExtractOpprettKritereFromDollyKriterier {

    public RsPersonKriteriumRequest execute(RsDollyPersonKriteriumRequest req){
        RsPersonKriterier rsPerson = new RsPersonKriterier();
        rsPerson.setAntall(req.getAntall());
        rsPerson.setIdenttype(req.getIdenttype());
        rsPerson.setKjonn(req.getKjonn());
        rsPerson.setFoedtEtter(req.getFoedtEtter());
        rsPerson.setFoedtFoer(req.getFoedtFoer());

        RsPersonKriteriumRequest kriteriumRequest = new RsPersonKriteriumRequest();
        kriteriumRequest.setPersonKriterierListe(Arrays.asList(rsPerson));
        kriteriumRequest.setWithAdresse(req.isWithAdresse());

        return kriteriumRequest;
    }

    public List<Person> addDollyKriterumValuesToPersonAndSave(RsDollyPersonKriteriumRequest req, List<Person> personer){
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
                }
        );

        return personer;
    }
}
