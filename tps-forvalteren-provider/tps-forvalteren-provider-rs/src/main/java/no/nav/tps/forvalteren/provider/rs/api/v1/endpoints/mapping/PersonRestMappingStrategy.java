package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import java.util.Collections;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonUtenRelasjon;
import no.nav.tps.forvalteren.domain.rs.RsSimplePerson;
import no.nav.tps.forvalteren.domain.rs.RsSivilstand;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentAlderFraIdent;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@Component
public class PersonRestMappingStrategy implements MappingStrategy {

    private static final String ID = "id";
    private static final String PERSON_ID = "personId";
    private static final String RELASJONER = "relasjoner";

    @Autowired
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Autowired
    private HentAlderFraIdent hentAlderFraIdent;

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Person.class, RsPerson.class)
                .field(ID, PERSON_ID)
                .customize(new CustomMapper<Person, RsPerson>() {
                    @Override public void mapAtoB(Person person, RsPerson rsPerson, MappingContext context) {
                        rsPerson.setFoedselsdato(hentDatoFraIdentService.extract(person.getIdent()));
                        rsPerson.setAlder(hentAlderFraIdent.extract(person.getIdent(), person.getDoedsdato()));
                        Collections.sort(rsPerson.getSivilstander(), Comparator.comparing(RsSivilstand::getSivilstandRegdato).reversed());
                    }
                })
                .byDefault()
                .register();

        factory.classMap(RsPerson.class, Person.class)
                .field(PERSON_ID, ID)
                .customize(new CustomMapper<RsPerson, Person>() {
                    @Override public void mapAtoB(RsPerson rsPerson, Person person, MappingContext context) {

                    }
                })
                .byDefault()
                .register();

        factory.classMap(Person.class, RsSimplePerson.class)
                .field(ID, PERSON_ID)
                .byDefault()
                .register();

        factory.classMap(Person.class, RsPersonUtenRelasjon.class)
                .field(ID, PERSON_ID)
                .customize(new CustomMapper<Person, RsPersonUtenRelasjon>() {
                    @Override public void mapAtoB(Person person, RsPersonUtenRelasjon rsPerson, MappingContext context) {
                        rsPerson.setFoedselsdato(hentDatoFraIdentService.extract(person.getIdent()));
                        rsPerson.setAlder(hentAlderFraIdent.extract(person.getIdent(), person.getDoedsdato()));
                        Collections.sort(rsPerson.getSivilstander(), Comparator.comparing(RsSivilstand::getSivilstandRegdato).reversed());
                    }
                })
                .exclude(RELASJONER)
                .byDefault()
                .register();
    }
}