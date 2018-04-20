package no.nav.tps.forvalteren.service.command.testdatamal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPerson;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;
import no.nav.tps.forvalteren.service.command.testdata.opprett.GetRandomFieldValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PersonmalPersonMapper{

    List<String> listOfRandomFields;

    private GetRandomFieldValues getRandomFieldValues;

    private BoundMapperFacade<RsPersonMal, Person> mapper;

    @Autowired
    public PersonmalPersonMapper(GetRandomFieldValues getRandomFieldValues) {
        this.getRandomFieldValues = getRandomFieldValues;
    }

    public PersonmalPersonMapper() {
        listOfRandomFields = new ArrayList<>();
    }


    public void register() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
            mapperFactory.classMap(RsPersonMal.class, Person.class)
                .customize(new CustomMapper<RsPersonMal, Person>() {
                    @Override
                    public void mapAtoB(RsPersonMal rsPersonMal, Person person, MappingContext mappingContext) {

                        for (Field field : rsPersonMal.getClass().getDeclaredFields()) {
                            try {

                                Object fieldValue = checkFieldValue(rsPersonMal, field);

                                if (fieldValue == "*") {

                                    getRandomFieldValues.execute(checkFieldName(field), person);

                                }

                            } catch (IllegalAccessException ex) {

                                System.out.println("failed");
                            }

                        }
                    }
                })
                .byDefault()
                .register();

    }

    private Object checkFieldValue(Object thisObj, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(thisObj);
    }

    private String checkFieldName(Field field) {
        return field.getName();
    }
}
