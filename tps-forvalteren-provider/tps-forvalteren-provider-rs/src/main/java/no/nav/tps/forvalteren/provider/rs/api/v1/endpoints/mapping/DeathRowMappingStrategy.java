package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import java.util.List;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import no.nav.tps.forvalteren.common.java.mapping.MappingStrategy;
import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRow;
import no.nav.tps.forvalteren.domain.rs.RsDeathRowBulk;
import org.springframework.stereotype.Component;

@Component
public class DeathRowMappingStrategy implements MappingStrategy {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(DeathRow.class, RsDeathRow.class)
                .field("endretDato", "tidspunkt")
                .field("endretAv", "bruker").byDefault().register();


        factory.classMap(RsDeathRow.class, DeathRow.class)
                .field("tidspunkt","endretDato")
                .field("bruker", "endretAv").byDefault().register();


        // TODO Create a customMapper for mapping DeathRow to Person.
        /*
        factory.classMap(DeathRow.class, Person.class)
                .customize(new CustomMapper<DeathRow, Person>() {
                    @Override
                    void mapAtoB(DeathRow deathrow, Person person, MappingContext context) {
                        //if(deathrow.getIdent() != null) {

                        person.setId(deathrow.getId());
                        person.setIdent(deathrow.getIdent());
                        person.setDoedsdato(LocalDateTime.of(deathrow.getDoedsdato(), LocalTime.now()));

                        return person;
                    }
                })

                .register();


        factory.classMap(Person.class, DeathRow.class)
                .customize(new CustomMapper<Person, DeathRow>() {
                     @Override
                     public Person mapAtoB(Person person, DeathRow deathrow, MappingContext context) {
                         //if(deathrow.getIdent() != null) {

                             person.setId(deathrow.getId());
                             person.setIdent(deathrow.getIdent());
                             person.setDoedsdato();

                             return person;
                     }

                })
                .byDefault()

                .register();
        */
        factory.classMap(RsDeathRowBulk.class, List.class)
                .customize(new CustomMapper<RsDeathRowBulk, List>() {
                    @Override
                    public void mapAtoB(RsDeathRowBulk rsDeathRowBulk, List deathRowList, MappingContext context) {
                        if (rsDeathRowBulk.getIdenter() != null) {
                            rsDeathRowBulk.getIdenter().forEach(ident -> {
                                deathRowList.add(DeathRow.builder()
                                        .id(rsDeathRowBulk.getId())
                                        .ident(ident)
                                        .tilstand(rsDeathRowBulk.getTilstand())
                                        .status(rsDeathRowBulk.getStatus())
                                        .miljoe(rsDeathRowBulk.getMiljoe())
                                        .handling(rsDeathRowBulk.getHandling())
                                        .doedsdato(rsDeathRowBulk.getDoedsdato())
                                        .build());
                            });
                        }
                    }
                })
                .register();
    }
}
