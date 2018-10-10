package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsSimplePerson;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentAlderFraIdent;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Component
public class PersonRestMappingStrategy implements MappingStrategy {

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private HentAlderFraIdent hentAlderFraIdent;

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Person.class, RsPerson.class)
                .field("id", "personId")
                .customize(new CustomMapper<Person, RsPerson>() {
                    @Override public void mapAtoB(Person person, RsPerson rsPerson, MappingContext context) {
                        rsPerson.setFoedselsdato(hentDatoFraIdentService.extract(person.getIdent()));
                        rsPerson.setAlder(hentAlderFraIdent.extract(person.getIdent(), person.getDoedsdato()));
                    }
                })
                .byDefault()
                .register();

        factory.classMap(Person.class, RsSimplePerson.class).field("id", "personId").byDefault().register();
    }
}