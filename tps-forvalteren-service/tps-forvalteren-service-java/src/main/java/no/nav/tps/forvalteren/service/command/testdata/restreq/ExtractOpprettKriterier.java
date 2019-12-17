package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.rs.RsBarnRequest.BorHos.MEG;
import static no.nav.tps.forvalteren.domain.rs.RsBarnRequest.BorHos.OSS;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.DNR;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.SPSF;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtEtter;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtFoer;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.assertj.core.util.Lists.newArrayList;

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
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.rs.RsAdresse;
import no.nav.tps.forvalteren.domain.rs.RsBarnRequest;
import no.nav.tps.forvalteren.domain.rs.RsPartnerRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;
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

        if (nonNull(request.getRelasjoner().getPartner())) {
            request.getRelasjoner().getPartnere().add(request.getRelasjoner().getPartner());
        }
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

        List<Adresse> adresser = isNull(req.getBoadresse()) || !req.getBoadresse().isValidAdresse() ?
                getAdresser(hovedPersoner.size() + partnere.size(), req.getAdresseNrInfo()) : new ArrayList();

        hovedPersoner.forEach(person -> {
            if (isBlank(req.getInnvandretFraLand())) {
                req.setInnvandretFraLand(landkodeEncoder.getRandomLandTla());
            }
            mapperFacade.map(req, person);
        });

        if (isNull(req.getBoadresse()) || !req.getBoadresse().isValidAdresse()) {
            for (int i = 0; i < hovedPersoner.size(); i++) {
                mapBoadresse(hovedPersoner.get(i), getBoadresse(adresser, i), extractFlyttedato(req.getBoadresse()));
            }
        }

        mapPartner(req, hovedPersoner, partnere, adresser);
        mapBarn(req, hovedPersoner, partnere, barn);

        List<Person> personer = new ArrayList<>();
        Stream.of(hovedPersoner, partnere, barn).forEach(personer::addAll);
        return personer;
    }

    private void mapPartner(RsPersonBestillingKriteriumRequest req, List<Person> hovedPersoner, List<Person> partnere, List<Adresse> adresser) {

        if (!req.getRelasjoner().getPartnere().isEmpty()) {
            int antallPartnere = partnere.size() / hovedPersoner.size();

            for (int i = 0; i < hovedPersoner.size(); i++) {
                int partnerStartIndex = antallPartnere * i;

                for (int j = 0; j < antallPartnere; j++) {
                    RsPartnerRequest partnerRequest = req.getRelasjoner().getPartnere().get(j);
                    partnerRequest.setPostadresse(
                            mapperFacade.mapAsList(nullcheckSetDefaultValue(partnerRequest.getPostadresse(), req.getPostadresse()), RsPostadresse.class));
                    mapperFacade.map(partnerRequest, partnere.get(partnerStartIndex + j));
                    Adresse adresse;
                    if (nonNull(partnerRequest.getBoadresse())) {
                        adresse = mapperFacade.map(partnerRequest.getBoadresse(), Adresse.class);
                    } else {
                        adresse = hasAdresseMedHovedperson(partnerRequest, j) && !hovedPersoner.get(i).getBoadresse().isEmpty() ?
                                hovedPersoner.get(i).getBoadresse().get(0) : getBoadresse(adresser, hovedPersoner.size() + partnerStartIndex + j);
                    }
                    mapBoadresse(partnere.get(partnerStartIndex + j), adresse, extractFlyttedato(partnerRequest.getBoadresse()));
                    alignStatsborgerskapAndInnvandretFraLand(partnere.get(partnerStartIndex + j), hovedPersoner.get(i));
                }
            }
        }
    }

    private void mapBarn(RsPersonBestillingKriteriumRequest req, List<Person> hovedPersoner, List<Person> partnere, List<Person> barn) {

        if (!req.getRelasjoner().getBarn().isEmpty()) {
            int antallBarn = barn.size() / hovedPersoner.size();
            int antallPartnere = partnere.isEmpty() ? 0 : partnere.size() / hovedPersoner.size();

            for (int i = 0; i < hovedPersoner.size(); i++) {
                int barnStartIndex = antallBarn * i;

                for (int j = 0; j < antallBarn; j++) {
                    RsBarnRequest barnRequest = req.getRelasjoner().getBarn().get(j);
                    barnRequest.setPostadresse(mapperFacade.mapAsList(nullcheckSetDefaultValue(barnRequest.getPostadresse(), req.getPostadresse()), RsPostadresse.class));
                    mapperFacade.map(barnRequest, barn.get(barnStartIndex + j));
                    mapBoadresse(barn.get(barnStartIndex + j),
                            (hasAdresseMedHovedperson(barnRequest) || antallPartnere == 0) && !hovedPersoner.get(i).getBoadresse().isEmpty() ?
                                    hovedPersoner.get(i).getBoadresse().get(0) :
                                    getPartnerAdresse(partnere, antallPartnere * i, barnRequest, getPartnerNr(j, antallPartnere)),
                            extractFlyttedato(barnRequest.getBoadresse()));
                    alignStatsborgerskapAndInnvandretFraLand(barn.get(barnStartIndex + j), hovedPersoner.get(i));
                    barn.get(barnStartIndex + j).setSivilstand(null);
                }
            }
        }
    }

    private static boolean hasAdresseMedHovedperson(RsBarnRequest barnRequest) {
        return !SPSF.name().equals(barnRequest.getSpesreg()) &&
                (isNull(barnRequest.getBorHos()) || OSS == barnRequest.getBorHos() || MEG == barnRequest.getBorHos());
    }

    private static boolean hasAdresseMedHovedperson(RsPartnerRequest partnerRequest, int partnerNumber) {
        return !SPSF.name().equals(partnerRequest.getSpesreg()) &&
                (TRUE.equals(partnerRequest.getHarFellesAdresse()) || isNull(partnerRequest.getHarFellesAdresse()) && partnerNumber == 0);
    }

    private static int getPartnerNr(int barnIndex, int totalPartners) {
        return totalPartners > 0 ? barnIndex % totalPartners : 0;
    }

    private static Adresse getPartnerAdresse(List<Person> partnere, int partnerStartIndex, RsBarnRequest barnRequest, int partnerNr) {
        Person partner = nonNull(barnRequest.getPartnerNr()) ?
                partnere.get(partnerStartIndex + barnRequest.getPartnerNr() - 1) :
                partnere.get(partnerStartIndex + partnerNr);

        return !SPSF.name().equals(partner.getSpesreg()) ? partner.getBoadresse().get(0) : null;
    }

    private static Adresse getBoadresse(List<Adresse> adresser, int index) {
        return !adresser.isEmpty() ? adresser.get(index % adresser.size()) : null;
    }

    private List<Adresse> getAdresser(int total, AdresseNrInfo adresseNrInfo) {

        return randomAdresseService.hentRandomAdresse(total, adresseNrInfo);
    }

    private static LocalDateTime extractFlyttedato(RsAdresse adresse) {
        return nonNull(adresse) ? adresse.getFlyttedato() : null;
    }

    private void alignStatsborgerskapAndInnvandretFraLand(Person person, Person hovedperson) {

        person.setInnvandretFraLand(nullcheckSetDefaultValue(person.getInnvandretFraLand(), hovedperson.getInnvandretFraLand()));

        if (!FNR.name().equals(person.getIdenttype()) && person.getStatsborgerskap().isEmpty()) {
            person.setStatsborgerskap(newArrayList(Statsborgerskap.builder()
                    .statsborgerskap(hovedperson.getStatsborgerskap().get(0).getStatsborgerskap())
                    .statsborgerskapRegdato(hentDatoFraIdentService.extract(person.getIdent()))
                    .person(person)
                    .build()));
        }
    }

    private void mapBoadresse(Person person, Adresse adresse, LocalDateTime flyttedato) {

        if (SPSF.name().equals(person.getSpesreg())) {
            return;
        }

        if (!DNR.name().equals(person.getIdenttype()) && !nonNull(person.getUtvandretTilLand()) && nonNull(adresse)) {

            Person tempPerson = adresse.getPerson();
            adresse.setPerson(null);

            // avoid Orika cyclic mapping overflow
            Adresse adresse1 = mapperFacade.map(adresse, Adresse.class);
            adresse.setPerson(tempPerson);

            adresse1.setFlyttedato(nullcheckSetDefaultValue(flyttedato,
                    hentDatoFraIdentService.extract(person.getIdent())));
            adresse1.setPerson(person);

            person.getBoadresse().add(adresse1);

        } else {

            dummyAdresseService.createDummyBoAdresse(person);
        }
    }
}