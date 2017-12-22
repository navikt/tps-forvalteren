package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsSimplePerson;

@Component
public class PersonRestMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Person.class, RsPerson.class).field("id", "personId").byDefault().register();
        factory.classMap(Person.class, RsSimplePerson.class).field("id", "personId").byDefault().register();

        factory.classMap(RsPerson.class, Person.class).field("personId", "id").byDefault().register();
        factory.classMap(RsSimplePerson.class, Person.class).field("personId", "id").byDefault().register();
    }

}
