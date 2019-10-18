package no.nav.tps.forvalteren.service.command.testdata.restreq;

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

    public static RsPersonKriteriumRequest extractMainPerson(RsPersonBestillingKriteriumRequest request) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(singletonList(RsPersonKriterier.builder()
                        .antall(nonNull(request.getAntall()) && request.getAntall() > 0 ? request.getAntall() : 1)
                        .identtype(nullcheckSetDefaultValue(request.getIdenttype(), "FNR"))
                        .kjonn(nullcheckSetDefaultValue(request.getKjonn(), "U"))
                        .foedtEtter(getProcessedFoedtEtter(request.getFoedtEtter(), request.getFoedtFoer(), false))
                        .foedtFoer(getProcessedFoedtFoer(request.getFoedtEtter(), request.getFoedtFoer(), false))
                        .harMellomnavn(request.getHarMellomnavn())
                        .build()))
                .build();
    }

    public static RsPersonKriteriumRequest extractPartner(RsPersonBestillingKriteriumRequest request) {

        List<RsPersonKriterier> kriterier = new ArrayList(request.getRelasjoner().getPartnere().size());
        request.getRelasjoner().getPartnere().forEach(partnerReq -> {
            RsPersonKriterier kriterium = prepareKriterium(partnerReq);
            kriterium.setFoedtEtter(getProcessedFoedtEtter(partnerReq.getFoedtEtter(), partnerReq.getFoedtFoer(), false));
            kriterium.setFoedtFoer(getProcessedFoedtFoer(partnerReq.getFoedtEtter(), partnerReq.getFoedtFoer(), false));
            kriterium.setHarMellomnavn(nullcheckSetDefaultValue(partnerReq.getHarMellomnavn(), request.getHarMellomnavn()));
            kriterier.add(kriterium);
        });

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(kriterier)
                .build();
    }

    public static RsPersonKriteriumRequest extractBarn(RsPersonBestillingKriteriumRequest request) {

        List<RsPersonKriterier> kriterier = new ArrayList(request.getRelasjoner().getBarn().size());
        request.getRelasjoner().getBarn().forEach(barnReq -> {
            RsPersonKriterier kriterium = prepareKriterium(barnReq);
            kriterium.setFoedtEtter(getProcessedFoedtEtter(barnReq.getFoedtEtter(), barnReq.getFoedtFoer(), true));
            kriterium.setFoedtFoer(getProcessedFoedtFoer(barnReq.getFoedtEtter(), barnReq.getFoedtFoer(), true));
            kriterium.setHarMellomnavn(nullcheckSetDefaultValue(barnReq.getHarMellomnavn(), request.getHarMellomnavn()));
            kriterier.add(kriterium);
        });

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
            if (isBlank(req.getInnvandretFraLand())) {
                req.setInnvandretFraLand(landkodeEncoder.getRandomLandTla());
            }
            mapperFacade.map(req, person);
        });

        if (isNull(req.getBoadresse())) {
            for (int i = 0; i < hovedPersoner.size(); i++) {
                mapBoadresse(hovedPersoner.get(i), null, !adresser.isEmpty() ? adresser.get(i % adresser.size()) : null, extractFlyttedato(req.getBoadresse()));
            }
        }

        mapPartner(req, hovedPersoner, partnere, adresser);
        mapBarn(req, hovedPersoner, barn, adresser);

        List<Person> personer = new ArrayList<>();
        Stream.of(hovedPersoner, partnere, barn).forEach(personer::addAll);
        return personer;
    }

    private void mapPartner(RsPersonBestillingKriteriumRequest req, List<Person> hovedPersoner, List<Person> partnere, List<Adresse> adresser) {
        if (!req.getRelasjoner().getPartnere().isEmpty()) {
            for (int i = 0; i < partnere.size(); i++) {
                req.getRelasjoner().getPartnere().get(i).setPostadresse(req.getPostadresse());
                mapperFacade.map(req.getRelasjoner().getPartnere().get(i), partnere.get(i));
                mapBoadresse(partnere.get(i), req.getBoadresse(), !adresser.isEmpty() ? adresser.get(i % adresser.size()) : null, extractFlyttedato(partnere.get(i).getBoadresse()));
                ammendDetailedPersonAttributes(req.getRelasjoner().getPartnere().get(i), partnere.get(i));
                partnere.get(i).setSivilstand(req.getSivilstand());
                partnere.get(i).setInnvandretFraLand(nullcheckSetDefaultValue(partnere.get(i).getInnvandretFraLand(), hovedPersoner.get(0).getInnvandretFraLand()));
            }
        }
    }

    private void mapBarn(RsPersonBestillingKriteriumRequest req, List<Person> hovedPersoner, List<Person> barn, List<Adresse> adresser) {
        if (!req.getRelasjoner().getBarn().isEmpty()) {
            for (int i = 0; i < barn.size(); i++) {
                req.getRelasjoner().getBarn().get(i).setPostadresse(req.getPostadresse());
                mapperFacade.map(req.getRelasjoner().getBarn().get(i), barn.get(i));
                mapBoadresse(barn.get(i), req.getBoadresse(), !adresser.isEmpty() ? adresser.get(i % adresser.size()) : null, extractFlyttedato(barn.get(i).getBoadresse()));
                ammendDetailedPersonAttributes(req.getRelasjoner().getBarn().get(i), barn.get(i));
                barn.get(i).setSivilstand(null);
                if (DNR.name().equals(barn.get(i).getIdenttype())) {
                    barn.get(i).setInnvandretFraLand(nullcheckSetDefaultValue(barn.get(i).getInnvandretFraLand(), hovedPersoner.get(0).getInnvandretFraLand()));
                }
            }
        }
    }

    private List<Adresse> getAdresser(int total, AdresseNrInfo adresseNrInfo) {

        return randomAdresseService.hentRandomAdresse(total, adresseNrInfo);
    }

    private static LocalDateTime extractFlyttedato(Adresse adresse) {
        return nonNull(adresse) ? adresse.getFlyttedato() : null;
    }

    private static LocalDateTime extractFlyttedato(RsAdresse adresse) {
        return nonNull(adresse) ? adresse.getFlyttedato() : null;
    }

    private static Person ammendDetailedPersonAttributes(RsSimplePersonRequest kriterier, Person person) {

        person.setStatsborgerskap(nullcheckSetDefaultValue(kriterier.getStatsborgerskap(), person.getStatsborgerskap()));
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