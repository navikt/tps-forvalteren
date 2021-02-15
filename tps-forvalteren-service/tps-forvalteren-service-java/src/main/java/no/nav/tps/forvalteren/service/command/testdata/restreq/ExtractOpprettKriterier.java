package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static no.nav.tps.forvalteren.domain.rs.RsRequestAdresse.TilleggType.CO_NAVN;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.DNR;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService.getRandomEtternavn;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService.getRandomFornavn;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtEtter;
import static no.nav.tps.forvalteren.service.command.testdata.restreq.DefaultBestillingDatoer.getProcessedFoedtFoer;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Matrikkeladresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.AdresseNrInfo;
import no.nav.tps.forvalteren.domain.rs.RsBarnRequest;
import no.nav.tps.forvalteren.domain.rs.RsPartnerRequest;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.RsPostadresse;
import no.nav.tps.forvalteren.domain.rs.RsRequestAdresse;
import no.nav.tps.forvalteren.domain.rs.RsRequestAdresse.TilleggAdressetype;
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.RandomAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExtractOpprettKriterier {

    private final MapperFacade mapperFacade;
    private final RandomAdresseService randomAdresseService;
    private final HentDatoFraIdentService hentDatoFraIdentService;
    private final LandkodeEncoder landkodeEncoder;
    private final DummyAdresseService dummyAdresseService;
    private final MidlertidigAdresseMappingService midlertidigAdresseMappingService;

    public static RsPersonKriteriumRequest extractMainPerson(RsPersonBestillingKriteriumRequest request) {

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(singletonList(RsPersonKriterier.builder()
                        .antall(nonNull(request.getAntall()) && request.getAntall() > 0 ? request.getAntall() : 1)
                        .identtype(nullcheckSetDefaultValue(request.getIdenttype(), "FNR"))
                        .kjonn(nullcheckSetDefaultValue(request.getKjonn(), KjoennType.U))
                        .foedtEtter(getProcessedFoedtEtter(request.getAlder(), request.getFoedtEtter(), request.getFoedtFoer(), false))
                        .foedtFoer(getProcessedFoedtFoer(request.getAlder(), request.getFoedtEtter(), request.getFoedtFoer(), false))
                        .harMellomnavn(request.getHarMellomnavn())
                        .navSyntetiskIdent(request.getNavSyntetiskIdent())
                        .build()))
                .build();
    }

    public static RsPersonKriteriumRequest extractPartner(List<RsPartnerRequest> partnerRequests,
            Boolean hovedpersonHarMellomnavn,
            Boolean navSyntetiskIdent) {

        List<RsPersonKriterier> kriterier = new ArrayList<>(partnerRequests.size());
        partnerRequests.forEach(partnerReq -> {
            RsPersonKriterier kriterium = prepareKriterium(partnerReq);
            kriterium.setFoedtEtter(getProcessedFoedtEtter(partnerReq.getAlder(), partnerReq.getFoedtEtter(), partnerReq.getFoedtFoer(), false));
            kriterium.setFoedtFoer(getProcessedFoedtFoer(partnerReq.getAlder(), partnerReq.getFoedtEtter(), partnerReq.getFoedtFoer(), false));
            kriterium.setHarMellomnavn(nullcheckSetDefaultValue(partnerReq.getHarMellomnavn(), hovedpersonHarMellomnavn));
            kriterium.setNavSyntetiskIdent(navSyntetiskIdent);
            kriterier.add(kriterium);
        });

        return RsPersonKriteriumRequest.builder()
                .personKriterierListe(kriterier)
                .build();
    }

    public static RsPersonKriteriumRequest extractBarn(List<RsBarnRequest> barnRequests,
            Boolean hovedpersonHarMellomnavn,
            Boolean navSyntetiskIdent) {

        List<RsPersonKriterier> kriterier = new ArrayList<>(barnRequests.size());
        barnRequests.forEach(barnReq -> {
            RsPersonKriterier kriterium = prepareKriterium(barnReq);
            kriterium.setFoedtEtter(getProcessedFoedtEtter(barnReq.getAlder(), barnReq.getFoedtEtter(), barnReq.getFoedtFoer(), true));
            kriterium.setFoedtFoer(getProcessedFoedtFoer(barnReq.getAlder(), barnReq.getFoedtEtter(), barnReq.getFoedtFoer(), true));
            kriterium.setHarMellomnavn(nullcheckSetDefaultValue(barnReq.getHarMellomnavn(), hovedpersonHarMellomnavn));
            kriterium.setNavSyntetiskIdent(navSyntetiskIdent);
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
                .kjonn(nullcheckSetDefaultValue(req.getKjonn(), KjoennType.U))
                .foedtFoer(req.getFoedtFoer())
                .foedtEtter(req.getFoedtEtter())
                .build();
    }

    public List<Person> addExtendedKriterumValuesToPerson(RsPersonBestillingKriteriumRequest req, Person hovedPerson, List<Person> partnere, List<Person> barn) {

        List<Adresse> adresser = getAdresser(1 + partnere.size(), req.getAdresseNrInfo());
        ammendBolignr(adresser, req);

        if (isBlank(req.getInnvandretFraLand())) {
            req.setInnvandretFraLand(landkodeEncoder.getRandomLandTla());
        }
        mapperFacade.map(req, hovedPerson);

        if (isNull(req.getBoadresse()) || !req.getBoadresse().isValidAdresse()) {
            mapBoadresse(hovedPerson, getBoadresse(adresser, 0), extractFlyttedato(req.getBoadresse()),
                    extractTilleggsadresse(req.getBoadresse()), null);
        }

        mapPartner(req, hovedPerson, partnere, adresser);
        mapBarn(req, hovedPerson, partnere, barn);
        midlertidigAdresseMappingService.mapAdresse(req, hovedPerson, partnere, barn);

        return Stream.of(singletonList(hovedPerson), partnere, barn)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void ammendBolignr(List<Adresse> adresser, RsPersonBestillingKriteriumRequest req) {

        if (nonNull(req.getBoadresse()) && isNotBlank(req.getBoadresse().getBolignr())) {
            adresser.get(0).setBolignr(req.getBoadresse().getBolignr());
        }
    }

    protected void mapPartner(RsPersonBestillingKriteriumRequest req, Person hovedPerson, List<Person> partnere, List<Adresse> adresser) {

        for (int i = 0; i < partnere.size(); i++) {
            RsPartnerRequest partnerRequest = req.getRelasjoner().getPartnere().get(i);
            partnerRequest.setPostadresse(
                    mapperFacade.mapAsList(
                            !partnerRequest.getPostadresse().isEmpty() ? partnerRequest.getPostadresse() : req.getPostadresse(),
                            RsPostadresse.class));
            mapperFacade.map(partnerRequest, partnere.get(i));
            mapPartnerAdresse(hovedPerson, partnere.get(i), getBoadresse(adresser, 1 + i), partnerRequest);
            alignStatsborgerskapAndInnvandretFraLand(partnere.get(i), hovedPerson);
        }
    }

    private void mapPartnerAdresse(Person hovedPerson, Person partner, Adresse adresse, RsPartnerRequest partnerRequest) {

        if (partner.isNyPerson()) {
            if (isTrue(partnerRequest.getHarFellesAdresse())) {
                mapBoadresse(partner, hovedPerson.getBoadresse().get(0), extractFlyttedato(partnerRequest.getBoadresse()),
                        extractTilleggsadresse(partnerRequest.getBoadresse()), null);

            } else if (nonNull(partnerRequest.getBoadresse())) {
                mapBoadresse(partner, mapperFacade.map(partnerRequest.getBoadresse(), Adresse.class), extractFlyttedato(partnerRequest.getBoadresse()),
                        extractTilleggsadresse(partnerRequest.getBoadresse()), null);

            } else {
                mapBoadresse(partner, adresse, extractFlyttedato(partnerRequest.getBoadresse()),
                        extractTilleggsadresse(partnerRequest.getBoadresse()), null);
            }
        }
    }

    protected void mapBarn(RsPersonBestillingKriteriumRequest req, Person hovedPerson, List<Person> partnere, List<Person> barn) {

        for (int i = 0; i < barn.size(); i++) {

            RsBarnRequest barnRequest = req.getRelasjoner().getBarn().get(i);

            barnRequest.setPostadresse(mapperFacade.mapAsList(
                    !barnRequest.getPostadresse().isEmpty() ? barnRequest.getPostadresse() : req.getPostadresse(),
                    RsPostadresse.class));
            mapperFacade.map(barnRequest, barn.get(i));
            mapBarnAdresse(hovedPerson, getPartner(partnere, barn, barnRequest), barn.get(i), barnRequest);
            alignStatsborgerskapAndInnvandretFraLand(barn.get(i), hovedPerson);
            barn.get(i).setSivilstand(null);
        }
    }

    private Person getPartner(List<Person> partnere, List<Person> barn, RsBarnRequest barnRequest) {
        return partnere.get(nonNull(barnRequest.getPartnerNr()) ? barnRequest.getPartnerNr() - 1 :
                barn.size() / partnere.size() % partnere.size());
    }

    private void mapBarnAdresse(Person hovedPerson, Person partner, Person barn, RsBarnRequest barnRequest) {

        if (hasAdresse(barnRequest) && barn.isNyPerson()) {

            if (hasBoadresse(hovedPerson) && barnRequest.isAdresseMedHovedPerson()) {
                mapBoadresse(barn, hovedPerson.getBoadresse().get(0), extractFlyttedato(barnRequest.getBoadresse()),
                        extractTilleggsadresse(barnRequest.getBoadresse()), null);
            }
            if (hasBoadresse(partner) && barnRequest.isAdresseMedPartner() && isAdresseDifferent(hovedPerson, partner)) {
                mapBoadresse(barn, partner.getBoadresse().get(0),
                        extractFlyttedato(barnRequest.getBoadresse()),
                        extractTilleggsadresse(barnRequest.getBoadresse()), barnRequest.isDeltAdresse());
            }
        }
    }

    private static boolean isAdresseDifferent(Person hovedperson, Person partner) {
        return hasBoadresse(hovedperson) && hasBoadresse(partner) &&
                !partner.getBoadresse().equals(hovedperson.getBoadresse());
    }

    private static Adresse getBoadresse(List<Adresse> adresser, int index) {
        return !adresser.isEmpty() ? adresser.get(index % adresser.size()) : null;
    }

    protected List<Adresse> getAdresser(int total, AdresseNrInfo adresseNrInfo) {
        return randomAdresseService.hentRandomAdresse(total, adresseNrInfo);
    }

    private static LocalDateTime extractFlyttedato(RsRequestAdresse adresse) {
        return nonNull(adresse) ? adresse.getFlyttedato() : null;
    }

    private static TilleggAdressetype extractTilleggsadresse(RsRequestAdresse adresse) {
        return nonNull((adresse)) ? adresse.getTilleggsadresse() : null;
    }

    private void alignStatsborgerskapAndInnvandretFraLand(Person person, Person hovedperson) {

        if (!person.isDoedFoedt()) {

            person.getInnvandretUtvandret().add(
                    InnvandretUtvandret.builder()
                            .innutvandret(INNVANDRET)
                            .landkode(nullcheckSetDefaultValue(person.getLandkodeOfFirstInnvandret(), hovedperson.getLandkodeOfFirstInnvandret()))
                            .flyttedato(nullcheckSetDefaultValue(person.getFlyttedatoOfFirstInnvandret(), hovedperson.getFlyttedatoOfFirstInnvandret()))
                            .person(person)
                            .build()
            );

            if (!FNR.name().equals(person.getIdenttype()) && person.getStatsborgerskap().isEmpty()) {
                person.setStatsborgerskap(newArrayList(Statsborgerskap.builder()
                        .statsborgerskap(hovedperson.getStatsborgerskap().get(0).getStatsborgerskap())
                        .statsborgerskapRegdato(hentDatoFraIdentService.extract(person.getIdent()))
                        .person(person)
                        .build()));
            }
        }
    }

    private static boolean hasAdresse(Person person) {
        return !person.isKode6() && !person.isUtenFastBopel() && !person.isForsvunnet() && !person.isDoedFoedt();
    }

    private static boolean hasBoadresse(Person person) {
        return nonNull(person) && !person.getBoadresse().isEmpty();
    }

    private static boolean hasAdresse(RsBarnRequest request) {
        return !request.isKode6() &&
                !request.isUtenFastBopel() &&
                isNull(request.getForsvunnetDato()) &&
                isNull(request.getUtvandretTilLand());
    }

    private void mapBoadresse(Person person, Adresse adresse, LocalDateTime flyttedato, TilleggAdressetype tilleggsadresse, Boolean isDelt) {

        if (!hasAdresse(person)) {
            return;
        }

        if (!DNR.name().equals(person.getIdenttype()) && !person.isUtvandret() && nonNull(adresse)) {

            Adresse adresse1 = adresse instanceof Gateadresse ?
                    Gateadresse.builder()
                            .adresse(((Gateadresse) adresse).getAdresse())
                            .husnummer(((Gateadresse) adresse).getHusnummer())
                            .gatekode(((Gateadresse) adresse).getGatekode())
                            .build() :
                    Matrikkeladresse.builder()
                            .mellomnavn(((Matrikkeladresse) adresse).getMellomnavn())
                            .gardsnr(((Matrikkeladresse) adresse).getGardsnr())
                            .bruksnr(((Matrikkeladresse) adresse).getBruksnr())
                            .festenr(((Matrikkeladresse) adresse).getFestenr())
                            .undernr(((Matrikkeladresse) adresse).getUndernr())
                            .build();

            adresse1.setKommunenr(adresse.getKommunenr());
            adresse1.setPostnr(adresse.getPostnr());
            adresse1.setBolignr(adresse.getBolignr());
            adresse1.setFlyttedato(nullcheckSetDefaultValue(flyttedato,
                    hentDatoFraIdentService.extract(person.getIdent())));

            adresse1.setTilleggsadresse(getTilleggAdresse(adresse.getTilleggsadresse(), tilleggsadresse));
            adresse1.setDeltAdresse(isDelt);
            adresse1.setPerson(person);

            person.getBoadresse().add(adresse1);

        } else {

            dummyAdresseService.createDummyBoAdresse(person);
        }
    }

    private static String getTilleggAdresse(String eksisterendeTAdresse, TilleggAdressetype tilleggAdressetype) {

        if (isNull(tilleggAdressetype)) {
            return eksisterendeTAdresse;
        }
        return CO_NAVN == tilleggAdressetype.getTilleggType() ?
                format("C/O %s %s", getRandomFornavn(), getRandomEtternavn()).toUpperCase() :
                format("%s: %s", tilleggAdressetype.getTilleggType(), tilleggAdressetype.getNummer())
                        .replace('_', '-');
    }
}