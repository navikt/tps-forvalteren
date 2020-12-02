package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsFullmakt;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonUtenIdenthistorikk;
import no.nav.tps.forvalteren.domain.rs.RsPersonUtenRelasjon;
import no.nav.tps.forvalteren.domain.rs.RsSimplePerson;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentAlderFraIdent;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

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
                .customize(new CustomMapper<>() {
                    @Override
                    public void mapAtoB(Person person, RsPerson rsPerson, MappingContext context) {
                        rsPerson.setFoedselsdato(hentDatoFraIdentService.extract(person.getIdent()));
                        rsPerson.setAlder(hentAlderFraIdent.extract(person.getIdent(), person.getDoedsdato()));
                        rsPerson.getFullmakt().addAll(
                                person.getFullmakt().stream().map(fullmakt -> RsFullmakt.builder()
                                        .fullmektig(fullmakt.getFullmektig())
                                        .gyldigFom(fullmakt.getGyldigFom())
                                        .gyldigTom(fullmakt.getGyldigTom())
                                        .kilde(fullmakt.getKilde())
                                        .omraader(Arrays.stream(fullmakt.getOmraader().split(",")).distinct().collect(Collectors.toList()))
                                        .person(person)
                                        .build())
                                        .collect(Collectors.toList()));
                        if (!person.getSivilstander().isEmpty()) {
                            rsPerson.setSivilstand(person.getSivilstander().get(0).getSivilstand());
                        }
                    }
                })
                .exclude("fullmakt")
                .byDefault()
                .register();

        factory.classMap(Person.class, RsPersonUtenIdenthistorikk.class)
                .field(ID, PERSON_ID)
                .customize(new CustomMapper<Person, RsPersonUtenIdenthistorikk>() {
                    @Override
                    public void mapAtoB(Person person, RsPersonUtenIdenthistorikk rsPerson, MappingContext context) {
                        rsPerson.setFoedselsdato(hentDatoFraIdentService.extract(person.getIdent()));
                        rsPerson.setAlder(hentAlderFraIdent.extract(person.getIdent(), person.getDoedsdato()));
                        if (!person.getSivilstander().isEmpty()) {
                            rsPerson.setSivilstand(person.getSivilstander().get(0).getSivilstand());
                        }
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
                    @Override
                    public void mapAtoB(Person person, RsPersonUtenRelasjon rsPerson, MappingContext context) {
                        rsPerson.setFoedselsdato(hentDatoFraIdentService.extract(person.getIdent()));
                        rsPerson.setAlder(hentAlderFraIdent.extract(person.getIdent(), person.getDoedsdato()));
                        if (!person.getSivilstander().isEmpty()) {
                            rsPerson.setSivilstand(person.getSivilstander().get(0).getSivilstand());
                        }
                    }
                })
                .exclude(RELASJONER)
                .byDefault()
                .register();
    }
}