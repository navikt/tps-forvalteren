package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.DNR;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.SPSF;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtEtter;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtFoer;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.rs.RsAdresse;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Slf4j
@Service
public class ExtractOpprettKriterier {

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private RandomAdresseService randomAdresseService;

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private LandkodeEncoder landkodeEncoder;

    @Autowired
    private DummyAdresseService dummyAdresseService;

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

        List<Adresse> adresser = isNull(req.getBoadresse()) ? getAdresser(hovedPersoner.size(), req.getAdresseNrInfo()) : new ArrayList();

        hovedPersoner.forEach(person -> {
            if (isBlank(person.getInnvandretFraLand())) {
                person.setInnvandretFraLand(landkodeEncoder.getRandomLandTla());
            }
            mapperFacade.map(req, person);
        });

        if (isNull(req.getBoadresse())) {
            for (int i = 0; i < hovedPersoner.size(); i++) {
                mapBoadresse(hovedPersoner.get(i), null, !adresser.isEmpty() ? adresser.get(i) : null, extractFlyttedato(req.getBoadresse()));
            }
        }

        if (nonNull(req.getRelasjoner().getPartner())) {
            for (int i = 0; i < partnere.size(); i++) {
                req.getRelasjoner().getPartner().setPostadresse(req.getPostadresse());
                mapperFacade.map(req.getRelasjoner().getPartner(), partnere.get(i));
                mapBoadresse(partnere.get(i), req.getBoadresse(), !adresser.isEmpty() ? adresser.get(i) : null, extractFlyttedato(partnere.get(i).getBoadresse()));
                ammendDetailedPersonAttributes(req.getRelasjoner().getPartner(), partnere.get(i));
                partnere.get(i).setSivilstand(req.getSivilstand());
                partnere.get(i).setInnvandretFraLand(nullcheckSetDefaultValue(partnere.get(i).getInnvandretFraLand(), hovedPersoner.get(0).getInnvandretFraLand()));
            }
        }
        if (!req.getRelasjoner().getBarn().isEmpty()) {
            for (int i = 0; i < barn.size(); i++) {
                req.getRelasjoner().getBarn().get(i).setPostadresse(req.getPostadresse());
                mapperFacade.map(req.getRelasjoner().getBarn().get(i), barn.get(i));
                mapBoadresse(barn.get(i), req.getBoadresse(), !adresser.isEmpty() ? adresser.get(i) : null, extractFlyttedato(barn.get(i).getBoadresse()));
                ammendDetailedPersonAttributes(req.getRelasjoner().getBarn().get(i), barn.get(i));
                barn.get(i).setSivilstand(null);
                barn.get(i).setInnvandretFraLand(nullcheckSetDefaultValue(barn.get(i).getInnvandretFraLand(), hovedPersoner.get(0).getInnvandretFraLand()));
            }
        }

        List<Person> personer = new ArrayList<>();
        Stream.of(hovedPersoner, partnere, barn).forEach(personer::addAll);
        return personer;
    }

    private List<Adresse> getAdresser(int total, AdresseNrInfo adresseNrInfo) {

        try {
            return randomAdresseService.hentRandomAdresse(total, adresseNrInfo);
        } catch (TpsfFunctionalException e) {
            log.warn("Adresseoppslag med type {} og verdi {} feilet {}", adresseNrInfo.getNummertype(), adresseNrInfo.getNummer(), e.getMessage());
        }
        return emptyList();
    }


    private static LocalDateTime extractFlyttedato(Adresse adresse) {
        return nonNull(adresse) ? adresse.getFlyttedato() : null;
    }

    private static LocalDateTime extractFlyttedato(RsAdresse adresse) {
        return nonNull(adresse) ? adresse.getFlyttedato() : null;
    }

    private static Person ammendDetailedPersonAttributes(RsSimplePersonRequest kriterier, Person person) {

        person.setStatsborgerskap(nullcheckSetDefaultValue(kriterier.getStatsborgerskap(), person.getStatsborgerskap()));
        person.setStatsborgerskapRegdato(nullcheckSetDefaultValue(kriterier.getStatsborgerskapRegdato(), person.getStatsborgerskapRegdato()));
        person.setSprakKode(nullcheckSetDefaultValue(kriterier.getSprakKode(), person.getSprakKode()));
        person.setDatoSprak(nullcheckSetDefaultValue(kriterier.getDatoSprak(), person.getDatoSprak()));

        return person;
    }

    private void mapBoadresse(Person person, RsAdresse rsAdresse, Adresse adresse, LocalDateTime flyttedato) {

        if (!DNR.name().equals(person.getIdenttype()) && !SPSF.name().equals(person.getSpesreg()) && !nonNull(person.getUtvandretTilLand())) {
            if (nonNull(rsAdresse) || nonNull(adresse)) {
                person.setBoadresse(mapperFacade.map(nonNull(rsAdresse) ? rsAdresse : adresse, Adresse.class));
                person.getBoadresse().setPerson(person);
                person.getBoadresse().setFlyttedato(nullcheckSetDefaultValue(flyttedato,
                        hentDatoFraIdentService.extract(person.getIdent())));
            } else {
                dummyAdresseService.createDummyBoAdresse(person);
            }
        }
    }
}