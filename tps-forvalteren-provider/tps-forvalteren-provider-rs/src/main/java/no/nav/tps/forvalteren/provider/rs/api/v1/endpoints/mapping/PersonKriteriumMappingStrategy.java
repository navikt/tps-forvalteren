package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;
import static no.nav.tps.forvalteren.domain.service.DiskresjonskoderType.UFB;
import static no.nav.tps.forvalteren.service.command.testdata.opprett.UfbAdresseUtil.createAdresseUfb;
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
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseUtil;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Component
public class PersonKriteriumMappingStrategy implements MappingStrategy {

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

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

                                if (UFB.name().equals(person.getSpesreg()) || kriteriumRequest.isUtenFastBopel()) {
                                    person.setBoadresse(createAdresseUfb(nonNull(kriteriumRequest.getBoadresse()) ? kriteriumRequest.getBoadresse().getKommunenr() : null));
                                } else {
                                    person.setBoadresse(nonNull(kriteriumRequest.getBoadresse()) ?
                                            mapperFacade.map(kriteriumRequest.getBoadresse(), Adresse.class) :
                                            DummyAdresseUtil.createDummyAdresse());
                                }

                                person.getBoadresse().setFlyttedato(nullcheckSetDefaultValue(person.getBoadresse().getFlyttedato(),
                                        hentDatoFraIdentService.extract(person.getIdent())));

                                person.getBoadresse().setPerson(person);

                                if (nonNull(kriteriumRequest.getPostadresse()) && !kriteriumRequest.getPostadresse().isEmpty()) {
                                    person.setPostadresse(mapperFacade.mapAsList(kriteriumRequest.getPostadresse(), Postadresse.class));
                                    person.getPostadresse().forEach(adr -> adr.setPerson(person));
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
}
