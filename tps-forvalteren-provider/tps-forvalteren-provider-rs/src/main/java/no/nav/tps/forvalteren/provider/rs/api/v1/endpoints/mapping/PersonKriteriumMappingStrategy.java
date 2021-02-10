package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolKjoenn.KVINNE;
import static no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolKjoenn.MANN;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.INNVANDRET;
import static no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret.InnUtvandret.UTVANDRET;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.DNR;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.UFB;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolKjoenn;
import no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolNewIdentsRequest;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.InnvandretUtvandret;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.jpa.Statsborgerskap;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.domain.rs.skd.KjoennType;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyLanguageService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.KontonrGeneratorService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.LandkodeEncoder;

@Component
@RequiredArgsConstructor
public class PersonKriteriumMappingStrategy implements MappingStrategy {

    private static final String KJONN = "kjonn";

    private final HentDatoFraIdentService hentDatoFraIdentService;
    private final DummyAdresseService dummyAdresseService;
    private final DummyLanguageService dummyLanguageService;
    private final KontonrGeneratorService kontonrGeneratorService;
    private final LandkodeEncoder landkodeEncoder;

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsPersonBestillingKriteriumRequest.class, Person.class)
                .customize(
                        new CustomMapper<>() {
                            @Override
                            public void mapAtoB(RsPersonBestillingKriteriumRequest kriteriumRequest, Person person, MappingContext context) {

                                person.setSikkerhetTiltakDatoFom(nullcheckSetDefaultValue(kriteriumRequest.getSikkerhetTiltakDatoFom(), now()));
                            }
                        })
                .exclude("regdato")
                .exclude("spesreg")
                .exclude("utenFastBopel")
                .exclude("egenAnsattDatoFom")
                .exclude("boadresse")
                .exclude("postadresse")
                .exclude("midlertidigAdresse")
                .exclude("identtype")
                .exclude(KJONN)
                .exclude("relasjoner")
                .exclude("vergemaal")
                .exclude("fullmakt")
                .byDefault()
                .register();

        factory.classMap(RsSimplePersonRequest.class, Person.class)
                .customize(
                        new CustomMapper<>() {
                            @Override
                            public void mapAtoB(RsSimplePersonRequest kriteriumRequest, Person person, MappingContext context) {

                                if (!person.isDoedFoedt()) {
                                    mapBasicProperties(kriteriumRequest, person);
                                    mapAdresser(kriteriumRequest, person, mapperFacade);
                                }
                                person.setGtVerdi(null);
                            }
                        })
                .exclude("ident")
                .exclude("identtype")
                .exclude("spesreg")
                .exclude("utenFastBopel")
                .exclude("egenAnsattDatoFom")
                .exclude("boadresse")
                .exclude("postadresse")
                .exclude("midlertidigAdresse")
                .exclude("identHistorikk")
                .exclude("statsborgerskap")
                .exclude("doedsdato")
                .exclude(KJONN)
                .byDefault()
                .register();

        factory.classMap(RsPersonKriterier.class, IdentpoolNewIdentsRequest.class)
                .customize(
                        new CustomMapper<>() {
                            @Override
                            public void mapAtoB(RsPersonKriterier personKriterier, IdentpoolNewIdentsRequest newIdentsRequest, MappingContext context) {

                                newIdentsRequest.setFoedtEtter(convert2LocalDate(personKriterier.getFoedtEtter()));
                                newIdentsRequest.setFoedtFoer(convert2LocalDate(personKriterier.getFoedtFoer()));
                                newIdentsRequest.setKjoenn(extractKjoenn(personKriterier.getKjonn()));
                                newIdentsRequest.setSyntetisk(personKriterier.getNavSyntetiskIdent());
                                newIdentsRequest.setRekvirertAv("TPSF");
                            }
                        })
                .exclude("foedtEtter")
                .exclude("foedtFoer")
                .exclude(KJONN)
                .byDefault()
                .register();
    }

    private void mapBasicProperties(RsSimplePersonRequest kriteriumRequest, Person person) {

        person.setIdenttype(nullcheckSetDefaultValue(person.getIdenttype(), "FNR"));
        person.setKjonn(nullcheckSetDefaultValue(person.getKjonn(), "U"));
        person.setRegdato(nullcheckSetDefaultValue(person.getRegdato(), now()));
        person.setDoedsdato(kriteriumRequest.getDoedsdato());

        mapMellomnavn(kriteriumRequest, person);

        mapStatsborgerskap(kriteriumRequest, person);

        person.setSprakKode(nullcheckSetDefaultValue(kriteriumRequest.getSprakKode(), DNR.name().equals(person.getIdenttype()) ? dummyLanguageService.getRandomLanguage() : "NB"));
        person.setDatoSprak(nullcheckSetDefaultValue(kriteriumRequest.getDatoSprak(),
                hentDatoFraIdentService.extract(person.getIdent())));

        if (FNR.name().equals(person.getIdenttype())) {
            person.setSpesreg(nullcheckSetDefaultValue(kriteriumRequest.getSpesreg(), kriteriumRequest.isUtenFastBopel() ? UFB.name() : null));
            person.setUtenFastBopel(kriteriumRequest.isUtenFastBopel());
        }
        person.setEgenAnsattDatoFom(kriteriumRequest.getEgenAnsattDatoFom());

        if (isNotBlank(person.getSpesreg())) {
            person.setSpesregDato(nullcheckSetDefaultValue(person.getSpesregDato(), hentDatoFraIdentService.extract(person.getIdent())));
        }

        if (isTrue(kriteriumRequest.getErForsvunnet()) && isNull(kriteriumRequest.getForsvunnetDato())) {
            person.setForsvunnetDato(now());
        }

        mapInnvandringUtvandring(kriteriumRequest, person);

        if (isTrue(kriteriumRequest.getHarBankkontonr())) {
            person.setBankkontonr(kontonrGeneratorService.generateNumber());
            person.setBankkontonrRegdato(nullcheckSetDefaultValue(kriteriumRequest.getBankkontonrRegdato(), LocalDateTime.now()));
        }
        person.setGtVerdi(null);
    }

    private void mapStatsborgerskap(RsSimplePersonRequest kriteriumRequest, Person person) {

        if (isNotBlank(kriteriumRequest.getStatsborgerskap())) {

            if (person.getStatsborgerskap().stream().noneMatch(statsborgerskap -> statsborgerskap.getStatsborgerskap().equals(kriteriumRequest.getStatsborgerskap()))) {
                person.getStatsborgerskap().add(Statsborgerskap.builder()
                        .statsborgerskap(kriteriumRequest.getStatsborgerskap())
                        .statsborgerskapRegdato(nullcheckSetDefaultValue(kriteriumRequest.getStatsborgerskapRegdato(), hentDatoFraIdentService.extract(person.getIdent())))
                        .statsborgerskapTildato(kriteriumRequest.getStatsborgerskapTildato())
                        .person(person)
                        .build());
            } else {
                person.getStatsborgerskap().stream().filter(statsborgerskap -> statsborgerskap.getStatsborgerskap().equals(kriteriumRequest.getStatsborgerskap())).forEach(statsborgerskap -> {
                    statsborgerskap.setStatsborgerskapRegdato(nullcheckSetDefaultValue(kriteriumRequest.getStatsborgerskapRegdato(), hentDatoFraIdentService.extract(person.getIdent())));
                    statsborgerskap.setStatsborgerskapTildato(kriteriumRequest.getStatsborgerskapTildato());
                });
            }

        } else if (person.getStatsborgerskap().isEmpty()) {

            person.getStatsborgerskap().add(Statsborgerskap.builder()
                    .statsborgerskap(FNR.name().equals(person.getIdenttype()) ? "NOR" :
                            landkodeEncoder.getRandomLandTla())
                    .statsborgerskapRegdato(nullcheckSetDefaultValue(kriteriumRequest.getInnvandretFraLandFlyttedato(),
                            hentDatoFraIdentService.extract(person.getIdent())))
                    .person(person)
                    .build());
        }
    }

    private void mapInnvandringUtvandring(RsSimplePersonRequest kriteriumRequest, Person person) {

        if (isNotBlank(kriteriumRequest.getInnvandretFraLand())) {
            person.getInnvandretUtvandret().add(
                    InnvandretUtvandret.builder()
                            .innutvandret(INNVANDRET)
                            .landkode(kriteriumRequest.getInnvandretFraLand())
                            .flyttedato(nullcheckSetDefaultValue(kriteriumRequest.getInnvandretFraLandFlyttedato(),
                                    hentDatoFraIdentService.extract(person.getIdent())))
                            .person(person)
                            .build());
        }

        if (isNotBlank(kriteriumRequest.getUtvandretTilLand())) {
            person.getInnvandretUtvandret().add(
                    InnvandretUtvandret.builder()
                            .innutvandret(UTVANDRET)
                            .landkode(kriteriumRequest.getUtvandretTilLand())
                            .flyttedato(nullcheckSetDefaultValue(kriteriumRequest.getUtvandretTilLandFlyttedato(),
                                    hentDatoFraIdentService.extract(person.getIdent()).plusYears(3)))
                            .person(person)
                            .build());
        }
    }

    private void mapAdresser(RsSimplePersonRequest kriteriumRequest, Person person, MapperFacade mapperFacade) {
        if (!kriteriumRequest.getPostadresse().isEmpty()) {
            List<Postadresse> postadresser = mapperFacade.mapAsList(kriteriumRequest.getPostadresse(), Postadresse.class);
            postadresser.forEach(adr -> adr.setPerson(person));
            person.getPostadresse().addAll(postadresser);
        }

        if (DNR.name().equals(person.getIdenttype())) {
            person.setBoadresse(null);
            if (kriteriumRequest.getPostadresse().isEmpty()) {
                person.getPostadresse().clear();
                person.getPostadresse().add(dummyAdresseService.createDummyPostAdresseUtland(person));
            }

        } else if (isNotBlank(kriteriumRequest.getUtvandretTilLand())) {
            if (person.getPostadresse().isEmpty() || "NOR".equals(person.getPostadresse().get(0).getPostLand())) {
                person.getPostadresse().add(dummyAdresseService.createPostAdresseUtvandret(person));
            }

        } else if (kriteriumRequest.isKode6()) {
            person.setBoadresse(null);
            person.getPostadresse().clear();
            person.getPostadresse().add(dummyAdresseService.createDummyPostAdresse(person));

        } else if (isUtenFastBopel(kriteriumRequest)) {
            person.getBoadresse().add(dummyAdresseService.createAdresseUfb(person, kriteriumRequest.getBoadresse()));

        } else if (nonNull(kriteriumRequest.getBoadresse()) && kriteriumRequest.getBoadresse().isValidAdresse()) {
            Adresse adresse = mapperFacade.map(kriteriumRequest.getBoadresse(), Adresse.class);
            adresse.setPerson(person);
            adresse.setFlyttedato(nullcheckSetDefaultValue(adresse.getFlyttedato(), hentDatoFraIdentService.extract(person.getIdent())));
            person.getBoadresse().add(adresse);
        }
        person.setGtVerdi(null);
    }

    private static void mapMellomnavn(RsSimplePersonRequest kriteriumRequest, Person person) {

        person.setMellomnavn(kriteriumRequest.hasMellomnavn() || isNotBlank(person.getMellomnavn()) ?
                PersonNameService.getRandomMellomnavn() : null);
    }

    private static boolean isUtenFastBopel(RsSimplePersonRequest request) {
        return "UFB".equals(request.getSpesreg()) || request.isUtenFastBopel();
    }

    private static IdentpoolKjoenn extractKjoenn(KjoennType kjoenn) {

        switch (kjoenn) {
            case K:
                return KVINNE;
            case M:
                return MANN;
            case U:
            default:
                return null;
        }
    }

    private static LocalDate convert2LocalDate(LocalDateTime dateTime) {
        return nonNull(dateTime) ? dateTime.toLocalDate() : null;
    }
}
