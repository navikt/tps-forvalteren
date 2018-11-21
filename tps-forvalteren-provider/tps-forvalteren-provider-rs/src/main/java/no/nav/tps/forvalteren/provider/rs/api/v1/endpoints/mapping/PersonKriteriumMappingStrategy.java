package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

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
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseOnPersonService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Component
public class PersonKriteriumMappingStrategy implements MappingStrategy {

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private DummyAdresseOnPersonService dummyAdresseOnPersonService;

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(RsPersonBestillingKriteriumRequest.class, Person.class)
                .customize(
                        new CustomMapper<RsPersonBestillingKriteriumRequest, Person>() {
                            @Override public void mapAtoB(RsPersonBestillingKriteriumRequest kriteriumRequest, Person person, MappingContext context) {

                                person.setStatsborgerskap((String) nullCheckSetDefaultValue(kriteriumRequest.getStatsborgerskap(), "NOR"));
                                person.setStatsborgerskapRegdato((LocalDateTime) nullCheckSetDefaultValue(kriteriumRequest.getStatsborgerskapRegdato(),
                                        hentDatoFraIdentService.extract(person.getIdent())));

                                person.setSprakKode((String) nullCheckSetDefaultValue(kriteriumRequest.getSprakKode(), "NB"));
                                person.setDatoSprak((LocalDateTime) nullCheckSetDefaultValue(kriteriumRequest.getDatoSprak(),
                                        hentDatoFraIdentService.extract(person.getIdent())));

                                if (kriteriumRequest.getBoadresse() != null) {
                                    person.setBoadresse(mapperFacade.map(kriteriumRequest.getBoadresse(), Adresse.class));
                                } else {
                                    dummyAdresseOnPersonService.execute(person);
                                }
                                if (person.getBoadresse().getFlyttedato() == null) {
                                    person.getBoadresse().setFlyttedato(hentDatoFraIdentService.extract(person.getIdent()));
                                }
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

    private Object nullCheckSetDefaultValue(Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }
}
