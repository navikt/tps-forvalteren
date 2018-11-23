package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static java.time.LocalDateTime.now;
import static no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.NullcheckUtil.nullcheckSetDefaultValue;

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
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresse;
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

                                person.setStatsborgerskap(nullcheckSetDefaultValue(kriteriumRequest.getStatsborgerskap(), "NOR"));
                                person.setStatsborgerskapRegdato(nullcheckSetDefaultValue(kriteriumRequest.getStatsborgerskapRegdato(),
                                        hentDatoFraIdentService.extract(person.getIdent())));

                                person.setSprakKode(nullcheckSetDefaultValue(kriteriumRequest.getSprakKode(), "NB"));
                                person.setDatoSprak(nullcheckSetDefaultValue(kriteriumRequest.getDatoSprak(),
                                        hentDatoFraIdentService.extract(person.getIdent())));

                                person.setSikkerhetsTiltakDatoFom(nullcheckSetDefaultValue(person.getSikkerhetsTiltakDatoFom(), now()));

                                person.setBoadresse(kriteriumRequest.getBoadresse() != null ?
                                        mapperFacade.map(kriteriumRequest.getBoadresse(), Adresse.class) :
                                        DummyAdresse.create());

                                person.getBoadresse().setFlyttedato(nullcheckSetDefaultValue(person.getBoadresse().getFlyttedato(),
                                        hentDatoFraIdentService.extract(person.getIdent())));

                                person.getBoadresse().setPerson(person);

                                if (kriteriumRequest.getPostadresse() != null && !kriteriumRequest.getPostadresse().isEmpty()) {
                                    person.setPostadresse(mapperFacade.mapAsList(kriteriumRequest.getPostadresse(), Postadresse.class));
                                    person.getPostadresse().forEach(adr -> adr.setPerson(person));
                                }
                            }
                        })

                .exclude("relasjoner")
                .byDefault()
                .register();
    }
}
