package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolKjoenn.KVINNE;
import static no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolKjoenn.MANN;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.DNR;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.UFB;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolKjoenn;
import no.nav.tps.forvalteren.consumer.rs.identpool.dao.IdentpoolNewIdentsRequest;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.rs.RsPersonKriterier;
import no.nav.tps.forvalteren.domain.rs.RsSimplePersonRequest;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyLanguageService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Component
public class PersonKriteriumMappingStrategy implements MappingStrategy {

    private static final String KJONN = "kjonn";

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private DummyAdresseService dummyAdresseService;

    @Autowired
    private DummyLanguageService dummyLanguageService;

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsPersonBestillingKriteriumRequest.class, Person.class)
                .customize(
                        new CustomMapper<RsPersonBestillingKriteriumRequest, Person>() {
                            @Override public void mapAtoB(RsPersonBestillingKriteriumRequest kriteriumRequest, Person person, MappingContext context) {

                                person.setSikkerhetsTiltakDatoFom(nullcheckSetDefaultValue(kriteriumRequest.getSikkerhetsTiltakDatoFom(), now()));
                            }
                        })
                .exclude("spesreg")
                .exclude("utenFastBopel")
                .exclude("egenAnsattDatoFom")
                .exclude("boadresse")
                .exclude("identtype")
                .exclude(KJONN)
                .exclude("relasjoner")
                .byDefault()
                .register();

        factory.classMap(RsSimplePersonRequest.class, Person.class)
                .customize(
                        new CustomMapper<RsSimplePersonRequest, Person>() {
                            @Override public void mapAtoB(RsSimplePersonRequest kriteriumRequest, Person person, MappingContext context) {

                                mapBasicProperties(kriteriumRequest, person);
                                mapAdresser(kriteriumRequest, person, mapperFacade);
                                person.setInnvandretFraLandFlyttedato(getInnvandretFraLandFlyttedato(person));
                            }
                        })
                .exclude("spesreg")
                .exclude("utenFastBopel")
                .exclude("egenAnsattDatoFom")
                .exclude("boadresse")
                .exclude("identtype")
                .exclude("identHistorikk")
                .exclude(KJONN)
                .byDefault()
                .register();

        factory.classMap(RsPersonKriterier.class, IdentpoolNewIdentsRequest.class)
                .customize(
                        new CustomMapper<RsPersonKriterier, IdentpoolNewIdentsRequest>() {
                            @Override public void mapAtoB(RsPersonKriterier personKriterier, IdentpoolNewIdentsRequest newIdentsRequest, MappingContext context) {

                                newIdentsRequest.setFoedtEtter(convertDate(personKriterier.getFoedtEtter()));
                                newIdentsRequest.setFoedtFoer(convertDate(personKriterier.getFoedtFoer()));
                                newIdentsRequest.setKjoenn(extractKjoenn(personKriterier.getKjonn()));
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

        if (isBlank(person.getStatsborgerskap())) {
            if (DNR.name().equals(person.getIdenttype())) {
                person.setStatsborgerskap(person.getInnvandretFraLand());
            }else{
                person.setStatsborgerskap("NOR");
            }
        }
        person.setStatsborgerskapRegdato(nullcheckSetDefaultValue(kriteriumRequest.getStatsborgerskapRegdato(),
                nullcheckSetDefaultValue(kriteriumRequest.getInnvandretFraLandFlyttedato(), hentDatoFraIdentService.extract(person.getIdent()))));

        person.setSprakKode(nullcheckSetDefaultValue(kriteriumRequest.getSprakKode(), DNR.name().equals(person.getIdenttype()) ? dummyLanguageService.getRandomLanguage() : "NB"));
        person.setDatoSprak(nullcheckSetDefaultValue(kriteriumRequest.getDatoSprak(),
                hentDatoFraIdentService.extract(person.getIdent())));

        if (FNR.name().equals(person.getIdenttype())) {
            person.setSpesreg(nullcheckSetDefaultValue(kriteriumRequest.getSpesreg(), kriteriumRequest.isUtenFastBopel() ? UFB.name() : null));
            person.setUtenFastBopel(kriteriumRequest.isUtenFastBopel());
            person.setEgenAnsattDatoFom(kriteriumRequest.getEgenAnsattDatoFom());
        }

        if (nonNull(person.getSpesreg())) {
            person.setSpesregDato(nullcheckSetDefaultValue(person.getSpesregDato(), hentDatoFraIdentService.extract(person.getIdent())));
        }

        if (TRUE.equals(kriteriumRequest.getErForsvunnet()) && isNull(kriteriumRequest.getForsvunnetDato())) {
            person.setForsvunnetDato(now());
        }
    }

    private void mapAdresser(RsSimplePersonRequest kriteriumRequest, Person person, MapperFacade mapperFacade) {
        if (!kriteriumRequest.getPostadresse().isEmpty()) {
            person.getPostadresse().clear();
            person.setPostadresse(mapperFacade.mapAsList(kriteriumRequest.getPostadresse(), Postadresse.class));
            person.getPostadresse().forEach(adr -> adr.setPerson(person));
        }

        if (DNR.name().equals(person.getIdenttype())) {
            person.setBoadresse(null);
            if (kriteriumRequest.getPostadresse().isEmpty()) {
                person.getPostadresse().clear();
                person.getPostadresse().add(dummyAdresseService.createDummyPostAdresseUtland(person));
            }

        } else if (isNotBlank(person.getUtvandretTilLand())) {
            person.setBoadresse(null);
            if (kriteriumRequest.getPostadresse().isEmpty() || "NOR".equals(kriteriumRequest.getPostadresse().get(0).getPostLand())) {
                person.getPostadresse().clear();
                person.getPostadresse().add(dummyAdresseService.createPostAdresseUtvandret(person));
                person.setUtvandretTilLandFlyttedato(nullcheckSetDefaultValue(kriteriumRequest.getUtvandretTilLandFlyttedato(), now()));
            }

        } else if (kriteriumRequest.isKode6()) {
            person.setBoadresse(null);
            person.getPostadresse().clear();
            person.getPostadresse().add(dummyAdresseService.createDummyPostAdresse(person));

        } else if (isUtenFastBopel(kriteriumRequest)) {
            person.getBoadresse().add(dummyAdresseService.createAdresseUfb(person, mapperFacade.map(kriteriumRequest.getBoadresse(), Adresse.class)));

        } else if (nonNull(kriteriumRequest.getBoadresse()) && kriteriumRequest.getBoadresse().isValidAdresse()) {
            Adresse adresse = mapperFacade.map(kriteriumRequest.getBoadresse(), Adresse.class);
            adresse.setPerson(person);
            adresse.setFlyttedato(nullcheckSetDefaultValue(adresse.getFlyttedato(), hentDatoFraIdentService.extract(person.getIdent())));
            person.getBoadresse().add(adresse);
        }
    }

    private static boolean isUtenFastBopel(RsSimplePersonRequest request) {
        return "UFB".equals(request.getSpesreg()) || request.isUtenFastBopel();
    }

    private static IdentpoolKjoenn extractKjoenn(String kjoenn) {

        switch (kjoenn) {
        case "K":
            return KVINNE;
        case "M":
            return MANN;
        case "U":
        default:
            return null;
        }
    }

    private static LocalDate convertDate(LocalDateTime dateTime) {
        return nonNull(dateTime) ? dateTime.toLocalDate() : null;
    }

    private LocalDateTime getInnvandretFraLandFlyttedato(Person person) {

        if (nonNull(person.getInnvandretFraLandFlyttedato())) {
            return person.getInnvandretFraLandFlyttedato();
        } else if (!person.getBoadresse().isEmpty() && nonNull(person.getBoadresse().get(0).getFlyttedato())) {
            return person.getBoadresse().get(0).getFlyttedato();
        } else {
            return hentDatoFraIdentService.extract(person.getIdent());
        }
    }
}
