package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.SPSF;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.UFB;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Postadresse;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Component
public class PersonKriteriumMappingStrategy implements MappingStrategy {

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private DummyAdresseService dummyAdresseService;

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsPersonBestillingKriteriumRequest.class, Person.class)
                .customize(
                        new CustomMapper<RsPersonBestillingKriteriumRequest, Person>() {
                            @Override public void mapAtoB(RsPersonBestillingKriteriumRequest kriteriumRequest, Person person, MappingContext context) {

                                person.setIdenttype(nullcheckSetDefaultValue(person.getIdenttype(), "FNR"));
                                person.setKjonn(nullcheckSetDefaultValue(person.getKjonn(), "U"));
                                person.setRegdato(nullcheckSetDefaultValue(person.getRegdato(), LocalDateTime.now()));

                                person.setStatsborgerskap(nullcheckSetDefaultValue(kriteriumRequest.getStatsborgerskap(), "NOR"));
                                person.setStatsborgerskapRegdato(nullcheckSetDefaultValue(kriteriumRequest.getStatsborgerskapRegdato(),
                                        hentDatoFraIdentService.extract(person.getIdent())));

                                person.setSprakKode(nullcheckSetDefaultValue(kriteriumRequest.getSprakKode(), "NB"));
                                person.setDatoSprak(nullcheckSetDefaultValue(kriteriumRequest.getDatoSprak(),
                                        hentDatoFraIdentService.extract(person.getIdent())));

                                person.setSpesreg(nullcheckSetDefaultValue(kriteriumRequest.getSpesreg(), kriteriumRequest.isUtenFastBopel() ? UFB.name() : null));

                                if (nonNull(person.getSpesreg())) {
                                    person.setSpesregDato(nullcheckSetDefaultValue(person.getSpesregDato(), hentDatoFraIdentService.extract(person.getIdent())));
                                }

                                person.setSikkerhetsTiltakDatoFom(nullcheckSetDefaultValue(person.getSikkerhetsTiltakDatoFom(), now()));

                                if (!kriteriumRequest.getPostadresse().isEmpty()) {
                                    person.getPostadresse().addAll(mapperFacade.mapAsList(kriteriumRequest.getPostadresse(), Postadresse.class));
                                    person.getPostadresse().forEach(adr -> adr.setPerson(person));
                                }

                                if (SPSF.name().equals(person.getSpesreg())) {
                                    person.setBoadresse(null);
                                    if (person.getPostadresse().isEmpty()) {
                                        person.getPostadresse().add(dummyAdresseService.createDummyPostAdresse(person));
                                    }

                                } else if (isUtenFastBopel(kriteriumRequest)) {
                                    person.setBoadresse(dummyAdresseService.createAdresseUfb(person));

                                } else if (nonNull(kriteriumRequest.getBoadresse())) {
                                    person.setBoadresse(mapperFacade.map(kriteriumRequest.getBoadresse(), Adresse.class));
                                    person.getBoadresse().setPerson(person);

                                } else {
                                    person.setBoadresse(dummyAdresseService.createDummyBoAdresse(person));
                                }
                            }
                        })
                .exclude("spesreg")
                .exclude("boadresse")
                .exclude("identtype")
                .exclude("kjonn")
                .exclude("relasjoner")
                .byDefault()
                .register();
    }

    private boolean isUtenFastBopel(RsPersonBestillingKriteriumRequest kriteriumRequest) {
        return (UFB.name().equals(kriteriumRequest.getSpesreg()) || kriteriumRequest.isUtenFastBopel()) && SPSF.name().equals(kriteriumRequest.getSpesreg());
    }
}
