package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.util.ArrayList;
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
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;

@Service
public class ExtractOpprettKriterier {

    @Autowired
    private MapperFacade mapperFacade;

    public static RsPersonKriteriumRequest extractMainPerson(RsPersonBestillingKriteriumRequest req) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(singletonList(RsPersonKriterier.builder()
                        .antall(nonNull(req.getAntall()) && req.getAntall() > 0 ? req.getAntall() : 1)
                        .identtype(nullcheckSetDefaultValue(req.getIdenttype(), "FNR"))
                        .kjonn(nullcheckSetDefaultValue(req.getKjonn(), "U"))
                        .foedtEtter(req.getFoedtEtter())
                        .foedtFoer(req.getFoedtFoer())
                        .build()))
                .build();
    }

    public static RsPersonKriteriumRequest extractPartner(RsSimplePersonRequest request) {

        RsPersonKriteriumRequest personRequestListe = new RsPersonKriteriumRequest();
        if (nonNull(request)) {
            addKriterium(personRequestListe, request);
        }

        return personRequestListe;
    }

    public static RsPersonKriteriumRequest extractBarn(List<RsSimplePersonRequest> request) {

        RsPersonKriteriumRequest personRequestListe = new RsPersonKriteriumRequest();
        for (RsSimplePersonRequest req : request) {
            addKriterium(personRequestListe, req);
        }

        return personRequestListe;
    }

    private static void addKriterium(RsPersonKriteriumRequest personRequestListe, RsSimplePersonRequest req) {
        personRequestListe.getPersonKriterierListe().add(RsPersonKriterier.builder()
                .antall(1)
                .identtype(nullcheckSetDefaultValue(req.getIdenttype(), "FNR"))
                .kjonn(nullcheckSetDefaultValue(req.getKjonn(), "U"))
                .foedtFoer(req.getFoedtFoer())
                .foedtEtter(req.getFoedtEtter())
                .build());
    }

    public List<Person> addExtendedKriterumValuesToPerson(RsPersonBestillingKriteriumRequest req, List<Person> hovedPersoner, List<Person> partnere, List<Person> barn) {

        hovedPersoner.forEach(person -> mapperFacade.map(req, person));
        if (nonNull(req.getRelasjoner().getPartner())) {
            partnere.forEach(partner -> {
                        mapperFacade.map(req, partner);
                        overrideDetailedPersonAttributes(req.getRelasjoner().getPartner(), partner);
                    }
            );
        }
        if (!req.getRelasjoner().getBarn().isEmpty()) {
            IntStream.range(0, barn.size()).forEach(i -> {
                mapperFacade.map(req, barn.get(i));
                overrideDetailedPersonAttributes(req.getRelasjoner().getBarn().get(i), barn.get(i));
            });
        }

        List<Person> personer = new ArrayList<>();
        Stream.of(hovedPersoner, partnere, barn).forEach(personer::addAll);
        return personer;
    }

    private Person overrideDetailedPersonAttributes(RsSimplePersonRequest kriterier, Person person) {

        person.setStatsborgerskap(nullcheckSetDefaultValue(kriterier.getStatsborgerskap(), person.getStatsborgerskap()));
        person.setStatsborgerskapRegdato(nullcheckSetDefaultValue(kriterier.getStatsborgerskapRegdato(), person.getStatsborgerskapRegdato()));
        person.setSprakKode(nullcheckSetDefaultValue(kriterier.getSprakKode(), person.getSprakKode()));
        person.setDatoSprak(nullcheckSetDefaultValue(kriterier.getDatoSprak(), person.getDatoSprak()));
        person.setSpesreg(nullcheckSetDefaultValue(kriterier.getSpesreg(), null));
        person.setSpesregDato(nullcheckSetDefaultValue(kriterier.getSpesregDato(), null));
        person.setEgenAnsattDatoFom(nullcheckSetDefaultValue(kriterier.getEgenAnsattDatoFom(), null));
        person.setEgenAnsattDatoTom(nullcheckSetDefaultValue(kriterier.getEgenAnsattDatoTom(), null));
        person.setSivilstand(null);  // partnere og barn skal ikke ha sivilstand -- foreløbig.

        return person;
    }
}