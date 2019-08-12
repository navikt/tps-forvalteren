package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtEtter;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtFoer;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetRandomAdresseOnPersons;

@Service
public class ExtractOpprettKriterier {

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private SetRandomAdresseOnPersons setRandomAdresseOnPersons;

    public static RsPersonKriteriumRequest extractMainPerson(RsPersonBestillingKriteriumRequest req) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(singletonList(RsPersonKriterier.builder()
                        .antall(nonNull(req.getAntall()) && req.getAntall() > 0 ? req.getAntall() : 1)
                        .identtype(nullcheckSetDefaultValue(req.getIdenttype(), "FNR"))
                        .kjonn(nullcheckSetDefaultValue(req.getKjonn(), "U"))
                        .foedtEtter(getProcessedFoedtEtter(req.getFoedtEtter(), req.getFoedtFoer(), false))
                        .foedtFoer(getProcessedFoedtFoer(req.getFoedtEtter(), req.getFoedtFoer(), false))
                        .harMellomnavn(req.getHarMellomnavn())
                        .build()))
                .build();
    }

    public static RsPersonKriteriumRequest extractPartner(RsPersonBestillingKriteriumRequest hovedPersonRequest) {

        List<RsPersonKriterier> kriterier = new ArrayList();
        if (nonNull(hovedPersonRequest.getRelasjoner().getPartner())) {
            RsSimplePersonRequest partnerReq = hovedPersonRequest.getRelasjoner().getPartner();
            RsPersonKriterier kriterium = prepareKriterium(hovedPersonRequest.getRelasjoner().getPartner());
            kriterium.setFoedtEtter(getProcessedFoedtEtter(partnerReq.getFoedtEtter(), partnerReq.getFoedtFoer(), false));
            kriterium.setFoedtFoer(getProcessedFoedtFoer(partnerReq.getFoedtEtter(), partnerReq.getFoedtFoer(), false));
            kriterium.setHarMellomnavn(nullcheckSetDefaultValue(hovedPersonRequest.getRelasjoner().getPartner().getHarMellomnavn(), hovedPersonRequest.getHarMellomnavn()));
            kriterier.add(kriterium);
        }

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(kriterier)
                .build();
    }

    public static RsPersonKriteriumRequest extractBarn(RsPersonBestillingKriteriumRequest hovedpersonRequest) {

        List<RsPersonKriterier> kriterier = new ArrayList(hovedpersonRequest.getRelasjoner().getBarn().size());
        for (RsSimplePersonRequest barnRequest : hovedpersonRequest.getRelasjoner().getBarn()) {
            RsPersonKriterier kriterium = prepareKriterium(barnRequest);
            kriterium.setFoedtEtter(getProcessedFoedtEtter(barnRequest.getFoedtEtter(), barnRequest.getFoedtFoer(), true));
            kriterium.setFoedtFoer(getProcessedFoedtFoer(barnRequest.getFoedtEtter(), barnRequest.getFoedtFoer(), true));
            kriterium.setHarMellomnavn(nullcheckSetDefaultValue(barnRequest.getHarMellomnavn(), hovedpersonRequest.getHarMellomnavn()));
            kriterier.add(kriterium);
        }

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(kriterier)
                .build();
    }

    private static RsPersonKriterier prepareKriterium(RsSimplePersonRequest req) {
        return RsPersonKriterier.builder()
                .antall(1)
                .identtype(nullcheckSetDefaultValue(req.getIdenttype(), "FNR"))
                .kjonn(nullcheckSetDefaultValue(req.getKjonn(), "U"))
                .foedtFoer(req.getFoedtFoer())
                .foedtEtter(req.getFoedtEtter())
                .build();
    }

    public List<Person> addExtendedKriterumValuesToPerson(RsPersonBestillingKriteriumRequest req, List<Person> hovedPersoner, List<Person> partnere, List<Person> barn) {

        hovedPersoner.forEach(person -> mapperFacade.map(req, person));

        if (isNull(req.getBoadresse())) {
            setRandomAdresseOnPersons.execute(hovedPersoner, req.getAdresseNrInfo());
        }

        if (nonNull(req.getRelasjoner().getPartner())) {
            partnere.forEach(partner -> {
                        req.getRelasjoner().getPartner().setPostadresse(req.getPostadresse());
                        mapperFacade.map(req.getRelasjoner().getPartner(), partner);
                        mapBoadresse(partner, hovedPersoner.get(0).getBoadresse());
                        ammendDetailedPersonAttributes(req.getRelasjoner().getPartner(), partner);
                        partner.setSivilstand(req.getSivilstand());
                        partner.setInnvandretFraLand(nullcheckSetDefaultValue(partner.getInnvandretFraLand(), hovedPersoner.get(0).getInnvandretFraLand()));
                    }
            );
        }
        if (!req.getRelasjoner().getBarn().isEmpty()) {
            IntStream.range(0, barn.size()).forEach(i -> {
                req.getRelasjoner().getBarn().get(i).setPostadresse(req.getPostadresse());
                mapperFacade.map(req.getRelasjoner().getBarn().get(i), barn.get(i));
                mapBoadresse(barn.get(i), hovedPersoner.get(0).getBoadresse());
                ammendDetailedPersonAttributes(req.getRelasjoner().getBarn().get(i), barn.get(i));
                barn.get(i).setSivilstand(null);
                barn.get(i).setInnvandretFraLand(nullcheckSetDefaultValue(barn.get(i).getInnvandretFraLand(), hovedPersoner.get(0).getInnvandretFraLand()));
            });
        }

        List<Person> personer = new ArrayList<>();
        Stream.of(hovedPersoner, partnere, barn).forEach(personer::addAll);
        return personer;
    }

    private static Person ammendDetailedPersonAttributes(RsSimplePersonRequest kriterier, Person person) {

        person.setStatsborgerskap(nullcheckSetDefaultValue(kriterier.getStatsborgerskap(), person.getStatsborgerskap()));
        person.setStatsborgerskapRegdato(nullcheckSetDefaultValue(kriterier.getStatsborgerskapRegdato(), person.getStatsborgerskapRegdato()));
        person.setSprakKode(nullcheckSetDefaultValue(kriterier.getSprakKode(), person.getSprakKode()));
        person.setDatoSprak(nullcheckSetDefaultValue(kriterier.getDatoSprak(), person.getDatoSprak()));

        return person;
    }

    private void mapBoadresse(Person targetPerson, Adresse hovedpersonAdresse) {

        Person person = hovedpersonAdresse.getPerson();
        hovedpersonAdresse.setPerson(null);
        targetPerson.setBoadresse(mapperFacade.map(hovedpersonAdresse, Adresse.class));
        hovedpersonAdresse.setPerson(person);
        targetPerson.getBoadresse().setPerson(targetPerson);
    }
}